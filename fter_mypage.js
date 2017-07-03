


// // 마이페이지 화면 들어갔을 때 100% 사용자가 쓴 글까지 함께
// router.get('/:user_nick', (req, res) => {
//   return new Promise((fulfill, reject) => {
//     pool.getConnection((err, connection) => {
//       if(err) reject(err);
//       else fulfill(connection);
//     });
//   })
//   .catch(err => { res.status(500).send({ result: [], message: 'getConnection error : '+err}); })
//   .then(connection => {
//     return new Promise((fulfill, reject) => {
//       let query = 'select level, profile, nickname, statemessage, part from User where nickname=?';
//       let query2 = 'select Post.title,Post.written_time,Post.id from User,Post where User.nickname=? and User.nickname=Post.user_nick order by Post.written_time desc';
//       connection.query(query,[req.params.user_nick],(err, data) => {
//           if(err) res.status(500).send({ message: 'selecting user error: '+err });
//           else {
//             console.log(data);
//             connection.query(query2,[req.params.user_nick, data], (err,data2)=> {
//               if(err) res.status(500).send({ message: 'second error: '+err });
//               else{
//                 console.log(data);
//                 let lecord = {
//                   level: data[0].level,
//                   profile: data[0].profile,
//                   nickname: data[0].nickname,
//                   statemessage: data[0].statemessage,
//                   part: data[0].part
//                 };
//                 var myPost_Info = [];
//                 for(var i=0;i<data2.length;i++){
//                   myPost_Info.push(data2[i]);
//                 }
//                 res.status(200).send({ result: { userinfo: lecord, postinfo : myPost_Info}, message: 'ok'});
//               }
//             });
//           }
//           connection.release();
//         });
//     });
//   })
// });


// 마이페이지 화면 들어갔을 때 100%
router.get('/:user_nick', (req, res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if (err) reject(err);
      else fulfill(connection);
    });
    })
    .catch(err => {
      res.status(500).send({ result: [], message: 'getConnection error : ' + err });
    })
    .then(connection => {
      return new Promise((fulfill, reject) => {
        let query = 'select level, profile, nickname, statemessage, part from User where nickname=?';
        //let query2 = 'select Post.title,Post.written_time,Post.id from User,Post where User.nickname=? and User.nickname=Post.user_nick order by Post.written_time desc';
        connection.query(query, [req.params.user_nick], (err, data) => {
          if (err) res.status(500).send({ message: 'selecting user error: ' + err });
          else {
            console.log(data);
            let lecord = {
              level: data[0].level,
              profile: data[0].profile,
              nickname: data[0].nickname,
              statemessage: data[0].statemessage,
              part: data[0].part
            };
            res.status(200).send({result: lecord, message: 'ok' });
          }
          connection.release();
        });
      });
    });
});

/////////// 내가 찜한 글 에서 다시 내가 쓴 글 눌렀을 때 100%
router.get('/write/:user_nick', (req, res) => {
  return new Promise((fulfill, reject) => {
      pool.getConnection((err, connection) => {
        if (err) reject(err);
        else fulfill(connection);
      });
    })
    .catch(err => {
      res.status(500).send({
        result: [],
        message: 'getConnection error : ' + err
      });
    })
    .then(connection => {
      let query = 'select Post.title,Post.written_time,Post.id from User,Post where User.nickname=? and User.nickname=Post.user_nick order by Post.written_time desc';
      return new Promise((fulfill, reject) => {
        connection.query(query, [req.params.user_nick], (err, data) => {
          if (err) res.status(500).send({
            result: [],
            message: 'selecting user error: ' + err
          });
          else res.status(200).send({
            result: data,
            message: 'ok'
          });
          connection.release();
        });
      });
    })
});

// 마이페이지에서 '내가 찜한 글' 눌렀을 시 100%
router.get('/like/:user_nick', (req, res) => {
  return new Promise((fulfill, reject) => {
      pool.getConnection((err, connection) => {
        if (err) reject(err);
        else fulfill(connection);
      });
    })
    .catch(err => {
      res.status(500).send({
        result: [],
        message: 'getConnection error : ' + err
      });
    })
    .then(connection => {
      let query = 'select Post.title,Post.written_time,Post.id from User,FavoritePost,Post where User.nickname=? and User.nickname=FavoritePost.user_nick and FavoritePost.post_id=Post.id order by Post.id desc';
      return new Promise((fulfill, reject) => {
        connection.query(query, [req.params.user_nick], (err, data) => {
          if (err) res.status(500).send({
            result: [],
            message: 'selecting user error: ' + err
          });
          else res.status(200).send({
            result: data,
            message: 'ok'
          });
          connection.release();
        });
      });
    })
});

// 개인정보 수정 100% 이미지 한장
router.post('/edit', upload.single('image'), function(req, res) {
  pool.getConnection(function(err, connection) {
    if (err) console.log('getConnection err: ', err);
    else {
      let userNick = req.body.user_nick;
      let query = 'update User set ? where nickname=?'; //query 순서중요. record 객체 아래에 query하면 imageurl 재대로 안넘어감
      let imageUrl;
      if (!req.file) imageUrl = null;
      else imageUrl = req.file.location;

      let record = {
        nickname: req.body.nickname,
        part: req.body.part,
        statemessage: req.body.statemessage,
        profile: imageUrl
      };

      connection.query(query, [record, userNick], function(err) {
        if (err) console.log('inserting query err:', err);
        else res.status(201).send({
          message: 'update'
        });
        connection.release();
      });
    }
  });
});

// // 개인정보 수정 100% 이미지 두장
// router.post('/edit', upload.array('image', 2), function(req, res) {
//   pool.getConnection(function(err, connection) {
//     if (err) console.log('getConnection err: ', err);
//     else {
//       let userNick = req.body.user_nick;
//       let query = 'update User set ? where nickname=?'; //query 순서중요. record 객체 아래에 query하면 imageurl 재대로 안넘어감
//
//       let record = {
//         nickname: req.body.nickname,
//         part: req.body.part,
//         statemessage: req.body.statemessage,
//         profile: req.files[0] ? req.files[0].location : null,
//         profileb: req.files[1] ? req.files[1].location : null
//       };
//
//       connection.query(query, [record, userNick], function(err) {
//         if (err) console.log('inserting query err:', err);
//         else res.status(201).send({
//           message: 'update'
//         });
//         connection.release();
//       });
//     }
//   });
// });

module.exports = router;
