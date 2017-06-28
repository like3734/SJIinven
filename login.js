const express = require('express');
const router = express.Router();
const aws = require('aws-sdk');
const bodyParser = require('body-parser');
const moment = require('moment');
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

const jsonParser = bodyParser.json(); // create application/json parser

//페북,카카오 연동 로그인
router.get('/:id', function(req, res) {
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
        userid = req.params.id;
        let query = 'select * from User where id = ? ';
        connection.query(query, userid, function(err, data) {
          if (err) reject([err, connection]);
          else fulfill([data, connection]);
        });
      });
    })
    .catch(values => {
      res.status(403).send({
        message: 'id check error' + values[0]
      });
      values[1].realease();
    })
    .then(values => {
      if (values[0].length === 0)
        res.status(201).send({
          message: 'new'
        });
      else
        res.status(201).send({
          message: 'old'
        });
      values[1].release();
    });
})

//닉네임 중복 검사
router.get('/profile/:nickname', function(req, res) {
  return new Promise((fulfill, reject) => {
      pool.getConnection((err, connection) => {
        if (err) reject(err);
        else fulfill(connection);
      })
    })
    .catch(err => {
      res.status(500).send({
        result: [],
        message: 'getConnection error :' + err
      });
    })
    .then((connection) => {
      return new Promise((fulfill, reject) => {
        let nick = req.params.nickname;
        let query = 'select * from User where nickname = ? ';
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
      console.log(values[0]);
      console.log(values[0][0]);
      if (values[0].length === 0) {// 검색결과 x
        res.status(201).send({
          message: 'true'
        });
      }
      else {
        res.status(201).send({
          message: 'false'
        });
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
          res.status(403).send({
            message: 'please input all of nickname, part.'
          });
        else {
          let query = 'insert into User set ?'; //3. 포스트 테이블에 게시글 저장
          let record = {
            id: userid,
            nickname: req.body.nickname,
            profile: req.file ? req.file.location : null,
            part: req.body.part,
            statemessage: req.body.statemessage
          };
          connection.query(query, record, err => {
            if (err) res.status(500).send({
              message: "inserting post error: " + err
            });
            else res.status(201).send({
              message: 'ok'
            });
          });
        }
        connection.release();
      });
    });
})


module.exports = router;
