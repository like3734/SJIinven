

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
                    written_time[d] = moment(written_time[d]).format('YYYY-MM-DD');
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
router.post('/relatedpost' , (req,res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: "getConnection err : " + err}) ; })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      connection.beginTransaction(err => {
        if(err) reject(err);
        else fulfill(connection);
      });
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: "Begin Transaction err : " + err}) ; })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      let query = 'update Alarm set readInfo=1 where id = ?';
      connection.query(query, req.body.alarm_id, (err, data) => {
        if(err) reject(err, connection);
        else fulfill(connection);
      });
    });
  })
  .catch(err => {
    res.status(500).send({ result: [], message: 'beginning transaction error: '+err});
    connection.rollback();
    connection.release();
  })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      console.log(req.body.user_nick);
      console.log(req.body.post_id);
      let query = ' select User.nickname, User.level, User.part as userpart , User.profile, User.statemessage, Post.title, Post.contents, Post.written_time, Post.part as postpart, Post.category, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5, Post.likecount, Post.commentcount from User, Post where User.nickname = Post.user_nick and Post.id = ? ';
      connection.query(query, req.body.post_id, (err, data) =>{
        if(err) reject([err, connection]);
        else fulfill([data, connection]);
      });
    });
  })
  .catch(([err, connection]) => {
    res.status(500).send({ result: [], message: "select info : " + err});
    connection.rollback();
    connection.release();
  })
  .then(([data, connection]) => {
    return new Promise((fulfill, reject) => {
      let query2 = 'select post_id from PostLikeCount where user_nick = ?' ;
      connection.query(query2, req.body.user_nick, (err, data2) => {
        if(err) reject([err, connection]);
        else fulfill([data, data2, connection]);
      });
    });
  })
  .catch(([err, connection]) => {
    res.status(500).send({ result: [], message: "select PostLikeCount err : " + err});
    connection.rollback();
    connection.release();
  })
  .then(([data, data2, connection]) => {
    return new Promise((fulfill, reject) => {
      let likecheck = 0;
      for(let a=0; a < data2.length ; a++){
        let id = data2[a].post_id;
        if(req.body.post_id == data2[a].post_id){
          likecheck = 1;
        }
      }
      let query3 = 'select post_id from FavoritePost where user_nick = ?';
      connection.query(query3, req.body.user_nick, (err, data3) => {
        if(err) reject([err, connection]);
        else fulfill([data, likecheck, data3, connection]);
      });
    });
  })
  .catch(([err, connection]) => {
    res.status(500).send({ result: [], message: "select FavoritePost err : " + err});
    connection.rollback();
    connection.release();
  })
  .then(([data, likecheck, data3, connection]) => {
    return new Promise((fulfill, reject) => {
      let ary_image = [];
      let imageInfo = [];
      let markcheck = 0;
      for(let a=0; a< data3.length ; a++){
        let id = data3[a].post_id;
        if(req.body.post_id == data3[a].post_id){
          markcheck = 1;
        }
      }
      imageInfo.push(data[0].image1);
      imageInfo.push(data[0].image2);
      imageInfo.push(data[0].image3);
      imageInfo.push(data[0].image4);
      imageInfo.push(data[0].image5);
      for(var d=0; d < 5; d++){
        if( imageInfo[d] !== null){
          ary_image.push(imageInfo[d]);
        }
      }

      let postinfo = {
        nickname: data[0].nickname,
        level: data[0].level,
        userpart: data[0].userpart,
        profile: data[0].profile,
        statemessage: data[0].statemessage,
        title: data[0].title,
        contents: data[0].contents,
        written_time: data[0].written_time,
        postpart: data[0].postpart,
        category: data[0].category,
        likecount: data[0].likecount,
        commentcount: data[0].commentcount,
        likecheck: likecheck,
        markcheck: markcheck,
        image: ary_image
      };
      let query4 = 'select Comment.user_nick, Comment.content, User.profile, Comment.written_time, User.level, User.part, User.statemessage from Comment, User where Comment.post_id = ? and Comment.user_nick = User.nickname order by Comment.id desc';
      connection.query(query4, req.body.post_id, (err, data4) => {
        if(err){
          res.status(500).send({ result: [], message: "select commentinfo err : " + err});
          connection.rollback();
        }
        else{
          let count=0;
          let ary_commentinfo = [];
          let now_time = new Date();
          let time_sub;
          let written_time = [];
          for(var i=0;i<data4.length;i++){
            written_time[i] =  data4[i].written_time;
            if(moment(written_time[i]).date() != now_time.getDate()){ // 같은 날이 아니면 그냥 날짜를 넣어줌
                written_time[i] =  moment(written_time[i]).format('YYYY-MM-DD');
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
                time_sub = (now_time.getHours()*60 + now_time.getMinutes()) - ( moment(written_time[i]).hours()*60 + moment(written_time[i]).minutes());
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
          }
          for (let c = 0; c < data4.length; c++) {
            if(count < 2){
              let commentinfo = {
                user_nick: data4[c].user_nick,
                content: data4[c].content,
                image: data4[c].profile,
                level: data4[c].level,
                part: data4[c].part,
                written_time: written_time[c],
                statemessage: data4[c].statemessage
              };
              ary_commentinfo.push(commentinfo);
              count++;
              console.log(count);
            }
          }
          console.log(ary_commentinfo[0]);
          let ary_all = { postinpo: postinfo, commentinfo: ary_commentinfo };
          res.status(200).send({
            result: ary_all,
            message: 'success'
          });
          connection.commit();
        }
        connection.release();
      });
    });
  })
})

module.exports = router;
