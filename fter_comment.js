
const jsonParser = bodyParser.json(); // create application/json parser

// 댓글 모두보기(+시간 계산 포함)
router.get('/:post_id' , (req,res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: 'getConnection error : '+err}); })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      let postid = req.params.post_id;
      let query = 'select Comment.user_nick, User.profile, Comment.content, Comment.written_time from User,Comment where User.nickname=Comment.user_nick and Comment.post_id = ? order by Comment.id asc';
        connection.query(query, postid, (err, data) => {
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
                  time_sub = now_time.getHours() - moment(written_time[i]).hours();
                  written_time[i] = time_sub+"시간 전";
                }
              }
              console.log(data[i].written_time);
            }

            for(var j=0;j<data.length;j++){
              record[j]={
                user_nick: data[j].user_nick,
                image: data[j].profile,
                content: data[j].content,
                written_time: written_time[j]
              };
            }
            res.status(200).send({ result : record, message: 'ok' });
          }
          connection.release();
        });
    });
  })
});


// 댓글 작성한 뒤 '전송'눌렀을 시
router.post('/add', (req, res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: 'getConnection error : '+err}); })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      let query = 'insert into Comment set ?';
      let record ={
        user_nick : req.body.user_nick,
        post_id : req.body.post_id,
        useful : req.body.useful,
        content : req.body.content,
        written_time : moment(new Date()).format('YYYY-MM-DD, HH:mm') // 게시글은 YY/MM/DD고 댓글은 분단위로 해줘야함
      };
      connection.query(query,record,(err, data) => {
          if(err) res.status(500).send({ message: 'selecting user error: '+err });
          else{
            res.status(200).send({ message: 'ok' });
          }
          connection.release();
        });
    });
  })
});

//유용한댓글 모두보기
router.get('/useful/:postid' , (req,res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: 'getConnection error : '+err}); })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      let postid = req.params.postid;
      let query = 'select Comment.user_nick, User.profile, Comment.content, Comment.written_time from User,Comment where User.nickname=Comment.user_nick and Comment.useful = 1 and Comment.post_id = ? order by Comment.id asc';
        connection.query(query, postid, (err, data) => {
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
            res.status(200).send({ result : record, message: 'ok' });
          }
          connection.release();
        });
    });
  })
});




module.exports = router;
