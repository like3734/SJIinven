const express = require('express');
const router = express.Router();
const aws = require('aws-sdk');
const moment = require('moment');
const bodyParser = require('body-parser');
//const ejs = require('ejs');
const fs = require('fs');
aws.config.loadFromPath('./config/aws_config.json');
const pool = require('../config/db_pool');
const multer = require('multer');
const multerS3 = require('multer-s3');
//const bcrypt = require('bcrypt');
var userid;
const s3 = new aws.S3();
const upload = multer({
  storage: multerS3({
    s3: s3,
    bucket: 'sjibk',
    acl: 'public-read',
    key: function(req, file, cb) {
      cb(null, Date.now() + '.' + file.originalname.split('.').pop());
    }
  })
});


// 마이페이지 화면 들어갔을 때 100%
router.get('/:user_nick', (req, res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: 'getConnection error : '+err}); })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      let query = 'select User.level,User.profile,User.nickname,User.statemessage,User.part from User where User.nickname=?';
      let query2 = 'select Post.title,Post.written_time from User,Post where User.nickname=? and User.nickname=Post.user_nick order by Post.written_time desc';
      connection.query(query,[req.params.user_nick],(err, data) => {
          if(err) res.status(500).send({ message: 'selecting user error: '+err });
          else {
            connection.query(query2,[req.params.user_nick],(err,data2)=>{
              if(err) res.status(500).send({ message: 'second error: '+err });
              else
                var myPost_Info = [];
                for(var i=0;i<data2.length;i++){
                  myPost_Info.push(data2[i]);
                }
                res.status(200).send({result:{level: data[0].level, profile: data[0].profile ,nickname: data[0].nickname,statemessage: data[0].statemessage, part: data[0].part, myPost : myPost_Info}});
            })
          }
          connection.release();
        });
    });
  })
});

/////////// 내가 찜한 글 에서 다시 내가 쓴 글 눌렀을 때 100%
router.get('/write/:user_nick', (req, res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: 'getConnection error : '+err}); })
  .then(connection => {
      let query = 'select Post.title,Post.written_time from User,Post where User.nickname=? and User.nickname=Post.user_nick order by Post.written_time desc';
      return new Promise((fulfill, reject) => {
      connection.query(query,[req.params.user_nick],(err, data) => {
          if(err) res.status(500).send({ result: [], message: 'selecting user error: '+err });
          else res.status(200).send({ result : data, message: 'ok' });
          connection.release();
        });
    });
  })
});

// 마이페이지에서 '내가 찜한 글' 눌렀을 시 100%
router.get('/like/:user_nick', (req, res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => { res.status(500).send({ result: [], message: 'getConnection error : '+err}); })
  .then(connection => {
      let query = 'select Post.title,Post.written_time from User,FavoritePost,Post where User.nickname=? and User.nickname=FavoritePost.user_nick and FavoritePost.post_id=Post.id order by Post.id desc';
      return new Promise((fulfill, reject) => {
      connection.query(query,[req.params.user_nick],(err, data) => {
          if(err) res.status(500).send({ result: [], message: 'selecting user error: '+err });
          else res.status(200).send({ result : data, message: 'ok' });
          connection.release();
        });
    });
  })
});

// 개인정보 수정 99% [ 이미지 후...]
router.put('/:user_nick', upload.single('image'), function(req, res) {
  pool.getConnection(function(err, connection) {
    if (err) console.log('getConnection err: ', err);
    else {
      let userNick = req.params.user_nick;
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
        else res.status(201).send({message: 'update'});
        connection.release();
      });
    }
  });
});

module.exports = router;
