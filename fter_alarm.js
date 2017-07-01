
// 알람창
router.get('/:user_nick', function(req, res) {
  return new Promise((fulfill, reject) => {
      pool.getConnection((err, connection) => {
        if (err) reject(err);
        else fulfill(connection);
      })
    })
    .catch(err => {
      res.status(500).send({ result: [], message: 'getConnection error : ' + err });
    })
    .then((connection) => {
      return new Promise((fulfill, reject) => {
        let usernick = req.params.user_nick;
        let query = 'select  Alarm.id, Alarm.comment_id, Alarm.readinfo, Comment.written_time from Comment, Alarm where Alarm.comment_id = Comment.id and Alarm.user_nick = ? order by Comment.written_time desc';
        let query2 = 'select Post.id as pid , Post.title, Comment.id as cid from Comment,Post where Post.id = Comment.post_id ';
        connection.query(query, usernick, function(err, data) {
          if (err) res.status(500).send({ result: [], message: 'select error : ' + err});
          else{
            let ary_alarminfo = [];
            let readcount=0;
            for(var a in data){
              let alarminfo = {
                id: data[a].id,
                commentid: data[a].comment_id,
                readinfo: data[a].readinfo,
                written_time: data[a].written_time,
              };
              ary_alarminfo.push(alarminfo);
            }
            //console.log(ary_alarminfo);
            connection.query(query2, (err, data2) => {
              if(err) res.status(500).send({ result: [], message: 'select2 error : ' + err});
              else{
                console.log(ary_alarminfo);
                console.log('--------');
                console.log(data2);
                let ary_postinfo= [];
                let ary_allinfo = [];
                let written_time = [];
                var now_time = new Date();
                for(var b in ary_alarminfo){
                  for(var c in data2){
                    if( ary_alarminfo[b].commentid === data2[c].cid ){
                      let postinfo = {
                        postid: data2[c].pid,
                        title: data2[c].title
                      };
                      ary_postinfo.push(postinfo);
                    }
                  }
                  if( ary_alarminfo[b].readinfo === 0 ) readcount++;
                }
                for(var d in ary_alarminfo){
                  written_time[d] = ary_alarminfo[d].written_time;
                  if(moment(written_time[d]).date() !== now_time.getDate())
                    written_time[d] = written_time[d];
                  else{
                    if(moment(written_time[d]).hours() === now_time.getHours()){
                      if(moment(written_time[d]).minutes() === now_time.getMinutes()){
                        written_time[d] = "방금 전";
                      }
                      else{
                        time_sub = now_time.getMinutes() - moment(written_time[d]).minutes();
                        written_time[d] = time_sub+"분 전";
                      }
                    }
                    else{
                      time_sub = now_time.getHours() - moment(written_time[d]).hours();
                      written_time[d] = time_sub+"시간 전";
                    }
                  }

                  let allinfo = {
                    id: ary_alarminfo[d].id,
                    readinfo: ary_alarminfo[d].readinfo,
                    written_time: written_time[d],
                    postid: ary_postinfo[d].postid,
                    title: ary_postinfo[d].title
                  };
                  ary_allinfo.push(allinfo);
                }

                res.status(200).send({ count: readcount, result: ary_allinfo, message: 'ok'});
              }
              connection.release();
            });
          } // query1 else
        });
      });
    })
})

// 알람창에서 댓글 모두 보기로 넘어가기
router.post('/allcomment' , (req,res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: 'getConnection error : '+err}); })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      let post_id = req.body.post_id;
      let alarm_id = req.body.alarm_id;

      let query = 'select Comment.user_nick, User.profile, Comment.content, Comment.written_time, Comment.id, Comment.post_id from User,Comment where User.nickname=Comment.user_nick and Comment.post_id = ? order by Comment.id asc';
        connection.query(query, post_id, (err, data) => {
          if(err) res.status(500).send({ result: [], message: 'comment all fail: '+err });
          else{
            var written_time = []; // 작성 시간
            var record = []; // 객체 배열
            var now_time = new Date(); // 현재 시간
            var time_sub = 0; // 시간차

            console.log(now_time);
            for(var i=0;i<data.length;i++){
              written_time[i] =  data[i].written_time;
              if(moment(written_time[i]).date() != now_time.getDate()){ // 같은 날이 아니면 그냥 날짜를 넣어줌
                  written_time[i] = written_time[i];
              }
              else{
                if(moment(written_time[i]).hours()==now_time.getHours()){ // 같은 날이고 시각도 같다면
                  if(moment(written_time[i]).minutes() == now_time.getMinutes()){ // '분'까지 같다면
                    written_time[i] = "방금 전";
                  }
                  else{ // 같은 날이고 시각도 같은데 '분'이 다르면
                    time_sub = now_time.getMinutes() - moment(written_time[i]).minutes();
                    written_time[i] = time_sub+"분 전";
                  }
                }
                else{ // 같은 날이고 시각이 다르다면
                  time_sub = (now_time.getMinutes()+60) - moment(written_time[i]).minutes();
                  if(time_sub >= 60){ // 두 시각의 차이가 60분이 넘는다면
                    time_sub = now_time.getHours() - moment(written_time[i]).hours();
                    written_time[i] = time_sub+"시간 전";
                  }
                  else{ // 두 시각의 차이가 60분이 넘지 않는다면
                    time_sub = (now_time.getMinutes()+60) - moment(written_time[i]).minutes();
                    written_time[i] = time_sub+"분 전";
                  }

                }
              }
              console.log(data[i].written_time);
            }

            for(var j=0;j<data.length;j++){
              record[j]={
                user_nick: data[j].user_nick,
                profile: data[j].profile,
                content: data[j].content,
                written_time: written_time[j]
              };
            }
              let query2 = 'update Alarm set readInfo=1 where id = ?';
              connection.query(query2,req.body.alarm_id,(err, data) => {
                  if(err) res.status(500).send({ result: [], message: 'fail'+err });
                  else res.status(200).send({ result : record, message: 'ok' });
                  connection.release();
              });
          }
        });
    });
  })
});

module.exports = router;
