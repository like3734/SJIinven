const express = require('express');
const router = express.Router();
const aws = require('aws-sdk');
const bodyParser = require('body-parser');
const moment = require('moment');
const fs = require('fs');
aws.config.loadFromPath('./config/aws_config.json');
const pool = require('../config/db_pool');
const multer = require('multer');
const multerS3 = require('multer-s3');
//const bcrypt = require('bcrypt');
const saltRounds = 10;
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

const jsonParser = bodyParser.json(); // create application/json parser

//페북,카카오 연동 로그인
router.post('/', function(req, res) {
  return new Promise((fulfill, reject) => {
      pool.getConnection((err, connection) => {
        if (err) reject(err);
        else fulfill(connection);
      })
    })
    .catch(err => {
      res.status(500).send({
        result: [],
        message: 'getConnection error : ' + err
      });
    })
    .then((connection) => {
      return new Promise((fulfill, reject) => {
        let comid = req.body.id;
        // var salt = bcrypt.genSaltSync(saltRounds);
        // var hash = bcrypt.hashSync(req.body.id, salt);
        let query = 'select userid from User where userid = ? ';
        connection.query(query, comid, function(err, data) {
          if (err) reject([err, connection]);
          else fulfill([data, comid, connection]);
        });
      });
    })
    .catch(values => {
      res.status(403).send({ result: [], message: 'id check error' + values[0] });
      values[1].release();
    })
    .then(values => {
      let comid = values[1];
      if(values[0].length === 0 ){
        let query2 = 'select count(*) as idcount from ID where id = ? ';
        values[2].query(query2, comid, (err, data2) =>{
          if(err) res.status(500).send({ result: [], message: 'id select error : ' + err});
          else{
            console.log(data2);
            console.log(data2[0].idcount);
            if(data2[0].idcount === 0){
              let query3 = 'insert into ID set ? ';
              let record = { id: comid };
              values[2].query(query3, record, (err,data3) => {
                if(err) res.status(500).send({ result: [], message: 'failed : ' + err});
                else res.status(201).send({ result: [], message: 'new'});
              });
            }
            else res.status(201).send({ result: [], message: 'new' });
            values[2].release();
          }
        });
      }
      else {
        let query4 = 'select * from User where userid = ? ';
        values[2].query(query4, comid, (err, data) =>{
          if(err) res.status(500).send({ result: [], message: 'data failed : ' + err});
          res.status(201).send({ result: data , message: 'old' });
          values[2].release();
        });
      }
    });
})

//닉네임 중복 검사
router.post('/nickcheck', function(req, res) {
  return new Promise((fulfill, reject) => {
      pool.getConnection((err, connection) => {
        if (err) reject(err);
        else fulfill(connection);
      })
    })
    .catch(err => {
      res.status(500).send({ result: [], message: 'getConnection error :' + err});
    })
    .then((connection) => {
      return new Promise((fulfill, reject) => {
        let nick = req.body.nickname;
        let query = 'select count(*) as nickcheck from User where nickname = ? ';
        connection.query(query, nick, function(err, data) {
          if (err) reject([err, connection]);
          else fulfill([data, connection]);
        });
        connection.release();
      });
    })
    .catch(values => {
      console.log("selecting query error: ", values[0]);
    })
    .then(values => {
      if (values[0][0].nickcheck === 0) {// 검색결과 x
        res.status(201).send({ message: 'true' });
      }
      else {
        res.status(201).send({ message: 'false' });
      }
    });
})


// 프로필 작성
router.post('/profile', upload.single('image'), function(req, res) {
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

    .then((connection) => {
      return new Promise((fulfill, reject) => {
        if (!(req.body.nickname && req.body.part))
          res.status(403).send({ message: 'please input all of nickname, part.' });
        else {
          let query = 'insert into User set ?' ; //3. 포스트 테이블에 게시글 저장
          let imageUri;
          if (!req.file) imageUri = null;
          else imageUri = req.file.location;
          let record = {
            userid: req.body.id,
            nickname: req.body.nickname,
            part: req.body.part,
            statemessage: req.body.statemessage,
            profile: imageUri
          };
          connection.query(query, record, err => {
            if (err) res.status(500).send({ message: "inserting post error: " + err });
            else res.status(201).send({ message: 'ok' });
            connection.release();
          });
        }
      });
    });
})


module.exports = router;
