const express = require('express');
const router = express.Router();
const aws = require('aws-sdk');
const bodyParser = require('body-parser');
//const ejs = require('ejs');
const fs = require('fs');
const moment = require('moment');
aws.config.loadFromPath('./config/aws_config.json');
const pool = require('../config/db_pool');
const multer = require('multer');
const multerS3 = require('multer-s3');
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

// 메인 list 띄워주기 + 최신순
router.get('/partlatest/:user_nick/:part', function(req, res) {
  pool.getConnection((err, connection) => {
    if (err) res.status(500).send({
      message: ' getConnection err : ' + err
    });
    else {
      let userpart = req.params.part;
      let usernick = req.params.user_nick;
      let query = 'select User.level, User.nickname, User.profile, Post.id, Post.title, Post.contents, Post.likecount, Post.written_time, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5 from User, Post where User.nickname = Post.user_nick and Post.part = ? order by Post.id desc';
      let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
      let query3 = 'select post_id from FavoritePost where user_nick = ? ';
      let query4 = 'select post_id, user_nick, content, image from Comment order by id desc';
      connection.query(query, userpart, function(err, data) {
        let ary_postinfo = [];
        for (var a in data) {
          let postinfo = {
            level: data[a].level,
            nickname: data[a].nickname,
            profile: data[a].profile,
            id: data[a].id,
            user_nick: data[a].user_nick,
            title: data[a].title,
            contents: data[a].contents,
            likecount: data[a].likecount,
            written_time: data[a].written_time,
            image0: data[a].image1,
            image1: data[a].image2,
            image2: data[a].image3,
            image3: data[a].image4,
            image4: data[a].image5
          };
          ary_postinfo.push(postinfo);
        }
        connection.query(query2, [usernick, ary_postinfo], function(err, data2) {
          let likeunlike = [];
          let allinfo = [];
          for (var b in ary_postinfo) {
            likeunlike[b] = 0;
            for (var c in data2) {
              if (ary_postinfo[b].id === data2[c].post_id) {
                likeunlike[b] = 1;
              }
            }
          }
          for (var c in ary_postinfo) {
            let info = {
              level: ary_postinfo[c].level,
              nickname: ary_postinfo[c].nickname,
              profile: ary_postinfo[c].profile,
              id: ary_postinfo[c].id,
              user_nick: ary_postinfo[c].user_nick,
              title: ary_postinfo[c].title,
              contents: ary_postinfo[c].contents,
              likecount: ary_postinfo[c].likecount,
              written_time: ary_postinfo[c].written_time,
              image0: ary_postinfo[c].image0,
              image1: ary_postinfo[c].image1,
              image2: ary_postinfo[c].image2,
              image3: ary_postinfo[c].image3,
              image4: ary_postinfo[c].image4,
              likecheck: likeunlike[c]
            };
            allinfo.push(info);
          }
          connection.query(query3, [usernick, allinfo], function(err, data3) {
            let markunmark = [];
            let getallinfo = [];
            for (var b in allinfo) {
              markunmark[b] = 0;
              for (var c in data3) {
                if (allinfo[b].id === data3[c].post_id) {
                  markunmark[b] = 1;
                }
              }
            }
            for (var c in allinfo) {
              let info = {
                level: allinfo[c].level,
                nickname: allinfo[c].nickname,
                profile: allinfo[c].profile,
                id: allinfo[c].id,
                user_nick: allinfo[c].user_nick,
                title: allinfo[c].title,
                contents: allinfo[c].contents,
                likecount: allinfo[c].likecount,
                written_time: allinfo[c].written_time,
                image0: allinfo[c].image0,
                image1: allinfo[c].image1,
                image2: allinfo[c].image2,
                image3: allinfo[c].image3,
                image4: allinfo[c].image4,
                likecheck: allinfo[c].likecheck,
                markcheck: markunmark[c]
              };
              getallinfo.push(info);
            }
            connection.query(query4, getallinfo, function(err, data4) {
              let ary_allinfo = new Array();
              let ary_commentinfo = new Array(new Array(), new Array(2));
              let count;
              for (var b in getallinfo) {
                count = 0;
                for (let c = 0; c < data4.length; c++) {
                  if (count !== 2 && getallinfo[b].id === data4[c].post_id) {
                    let commentinfo = {
                      user_nick: data4[c].user_nick,
                      content: data4[c].content,
                      image: data4[c].image
                    };
                    ary_commentinfo[b][count] = commentinfo;
                    count++;
                  };
                }
                if (ary_commentinfo[b][0] === undefined) {
                  let n_commentinfo = {
                    user_nick: " ",
                    content: " ",
                    image: " "
                  };
                  ary_commentinfo[b][0] = n_commentinfo;
                  ary_commentinfo[b][1] = n_commentinfo;
                } else if (ary_commentinfo[b][1] === undefined) {
                  let n_commentinfo = {
                    user_nick: " ",
                    content: " ",
                    image: " "
                  };
                  ary_commentinfo[b][1] = n_commentinfo;
                } else {};
              }
              for (var d in getallinfo) {
                let joininfo = {
                  postinfo: getallinfo[d],
                  commentinfo: ary_commentinfo[d]
                };
                ary_allinfo[d] = joininfo;
              }
              res.status(203).send({
                result: ary_allinfo,
                message: 'ok'
              });
            });

          });
          connection.release();
        });
      });
    }
  });
});

// 파트별 인기순 보기 100%
router.get('/partpopular/:user_nick/:part', function(req, res) {
  pool.getConnection((err, connection) => {
    if (err) res.status(500).send({
      message: ' getConnection err : ' + err
    });
    else {
      let userpart = req.params.part;
      let usernick = req.params.user_nick;
      let query = 'select User.level, User.nickname, User.profile, Post.id, Post.title, Post.contents, Post.likecount, Post.written_time, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5 from User, Post where User.nickname = Post.user_nick and Post.part = ? order by Post.likecount desc';
      let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
      let query3 = 'select post_id from FavoritePost where user_nick = ? ';
      let query4 = 'select post_id, user_nick, content, image from Comment order by id desc';
      connection.query(query, userpart, function(err, data) {
        let ary_postinfo = [];
        for (var a in data) {
          let postinfo = {
            level: data[a].level,
            nickname: data[a].nickname,
            profile: data[a].profile,
            id: data[a].id,
            user_nick: data[a].user_nick,
            title: data[a].title,
            contents: data[a].contents,
            likecount: data[a].likecount,
            written_time: data[a].written_time,
            image0: data[a].image1,
            image1: data[a].image2,
            image2: data[a].image3,
            image3: data[a].image4,
            image4: data[a].image5
          };
          ary_postinfo.push(postinfo);
        }
        connection.query(query2, [usernick, ary_postinfo], function(err, data2) {
          let likeunlike = [];
          let allinfo = [];
          for (var b in ary_postinfo) {
            likeunlike[b] = 0;
            for (var c in data2) {
              if (ary_postinfo[b].id === data2[c].post_id) {
                likeunlike[b] = 1;
              }
            }
          }
          for (var c in ary_postinfo) {
            let info = {
              level: ary_postinfo[c].level,
              nickname: ary_postinfo[c].nickname,
              profile: ary_postinfo[c].profile,
              id: ary_postinfo[c].id,
              user_nick: ary_postinfo[c].user_nick,
              title: ary_postinfo[c].title,
              contents: ary_postinfo[c].contents,
              likecount: ary_postinfo[c].likecount,
              written_time: ary_postinfo[c].written_time,
              image0: ary_postinfo[c].image0,
              image1: ary_postinfo[c].image1,
              image2: ary_postinfo[c].image2,
              image3: ary_postinfo[c].image3,
              image4: ary_postinfo[c].image4,
              likecheck: likeunlike[c]
            };
            allinfo.push(info);
          }
          connection.query(query3, [usernick, allinfo], function(err, data3) {
            let markunmark = [];
            let getallinfo = [];
            for (var b in allinfo) {
              markunmark[b] = 0;
              for (var c in data3) {
                if (allinfo[b].id === data3[c].post_id) {
                  markunmark[b] = 1;
                }
              }
            }
            for (var c in allinfo) {
              let info = {
                level: allinfo[c].level,
                nickname: allinfo[c].nickname,
                profile: allinfo[c].profile,
                id: allinfo[c].id,
                user_nick: allinfo[c].user_nick,
                title: allinfo[c].title,
                contents: allinfo[c].contents,
                likecount: allinfo[c].likecount,
                written_time: allinfo[c].written_time,
                image0: allinfo[c].image0,
                image1: allinfo[c].image1,
                image2: allinfo[c].image2,
                image3: allinfo[c].image3,
                image4: allinfo[c].image4,
                likecheck: allinfo[c].likecheck,
                markcheck: markunmark[c]
              };
              getallinfo.push(info);
            }
            connection.query(query4, getallinfo, function(err, data4) {
              let ary_allinfo = new Array();
              let ary_commentinfo = new Array(new Array(), new Array(2));
              let count;
              for (var b in getallinfo) {
                count = 0;
                for (let c = 0; c < data4.length; c++) {
                  if (count !== 2 && getallinfo[b].id === data4[c].post_id) {
                    let commentinfo = {
                      user_nick: data4[c].user_nick,
                      content: data4[c].content,
                      image: data4[c].image
                    };
                    ary_commentinfo[b][count] = commentinfo;
                    count++;
                  };
                }
                if (ary_commentinfo[b][0] === undefined) {
                  let n_commentinfo = {
                    user_nick: " ",
                    content: " ",
                    image: " "
                  };
                  ary_commentinfo[b][0] = n_commentinfo;
                  ary_commentinfo[b][1] = n_commentinfo;
                } else if (ary_commentinfo[b][1] === undefined) {
                  let n_commentinfo = {
                    user_nick: " ",
                    content: " ",
                    image: " "
                  };
                  ary_commentinfo[b][1] = n_commentinfo;
                } else {};
              }
              for (var d in getallinfo) {
                let joininfo = {
                  postinfo: getallinfo[d],
                  commentinfo: ary_commentinfo[d]
                };
                ary_allinfo[d] = joininfo;
              }
              res.status(203).send({
                result: ary_allinfo,
                message: 'ok'
              });
            });

          });
          connection.release();
        });
      });
    }
  });
});

// 카테고리별 최신순 보기 100%
router.get('/categorylatest/:user_nick/:category', function(req, res) {
  pool.getConnection((err, connection) => {
    if (err) res.status(500).send({
      message: ' getConnection err : ' + err
    });
    else {
      let category = req.params.category;
      let usernick = req.params.user_nick;
      let query = 'select User.level, User.nickname, User.profile, Post.id, Post.title, Post.contents, Post.likecount, Post.written_time, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5 from User, Post where User.nickname = Post.user_nick and Post.category = ? order by Post.id desc';
      let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
      let query3 = 'select post_id from FavoritePost where user_nick = ? ';
      let query4 = 'select post_id, user_nick, content, image from Comment order by id desc';
      connection.query(query, category, function(err, data) {
        let ary_postinfo = [];
        for (var a in data) {
          let postinfo = {
            level: data[a].level,
            nickname: data[a].nickname,
            profile: data[a].profile,
            id: data[a].id,
            user_nick: data[a].user_nick,
            title: data[a].title,
            contents: data[a].contents,
            likecount: data[a].likecount,
            written_time: data[a].written_time,
            image0: data[a].image1,
            image1: data[a].image2,
            image2: data[a].image3,
            image3: data[a].image4,
            image4: data[a].image5
          };
          ary_postinfo.push(postinfo);
        }
        connection.query(query2, [usernick, ary_postinfo], function(err, data2) {
          let likeunlike = [];
          let allinfo = [];
          for (var b in ary_postinfo) {
            likeunlike[b] = 0;
            for (var c in data2) {
              if (ary_postinfo[b].id === data2[c].post_id) {
                likeunlike[b] = 1;
              }
            }
          }
          for (var c in ary_postinfo) {
            let info = {
              level: ary_postinfo[c].level,
              nickname: ary_postinfo[c].nickname,
              profile: ary_postinfo[c].profile,
              id: ary_postinfo[c].id,
              user_nick: ary_postinfo[c].user_nick,
              title: ary_postinfo[c].title,
              contents: ary_postinfo[c].contents,
              likecount: ary_postinfo[c].likecount,
              written_time: ary_postinfo[c].written_time,
              image0: ary_postinfo[c].image0,
              image1: ary_postinfo[c].image1,
              image2: ary_postinfo[c].image2,
              image3: ary_postinfo[c].image3,
              image4: ary_postinfo[c].image4,
              likecheck: likeunlike[c]
            };
            allinfo.push(info);
          }
          connection.query(query3, [usernick, allinfo], function(err, data3) {
            let markunmark = [];
            let getallinfo = [];
            for (var b in allinfo) {
              markunmark[b] = 0;
              for (var c in data3) {
                if (allinfo[b].id === data3[c].post_id) {
                  markunmark[b] = 1;
                }
              }
            }
            for (var c in allinfo) {
              let info = {
                level: allinfo[c].level,
                nickname: allinfo[c].nickname,
                profile: allinfo[c].profile,
                id: allinfo[c].id,
                user_nick: allinfo[c].user_nick,
                title: allinfo[c].title,
                contents: allinfo[c].contents,
                likecount: allinfo[c].likecount,
                written_time: allinfo[c].written_time,
                image0: allinfo[c].image0,
                image1: allinfo[c].image1,
                image2: allinfo[c].image2,
                image3: allinfo[c].image3,
                image4: allinfo[c].image4,
                likecheck: allinfo[c].likecheck,
                markcheck: markunmark[c]
              };
              getallinfo.push(info);
            }
            connection.query(query4, getallinfo, function(err, data4) {
              let ary_allinfo = new Array();
              let ary_commentinfo = new Array(new Array(), new Array(2));
              let count;
              for (var b in getallinfo) {
                count = 0;
                for (let c = 0; c < data4.length; c++) {
                  if (count !== 2 && getallinfo[b].id === data4[c].post_id) {
                    let commentinfo = {
                      user_nick: data4[c].user_nick,
                      content: data4[c].content,
                      image: data4[c].image
                    };
                    ary_commentinfo[b][count] = commentinfo;
                    count++;
                  };
                }
                if (ary_commentinfo[b][0] === undefined) {
                  let n_commentinfo = {
                    user_nick: " ",
                    content: " ",
                    image: " "
                  };
                  ary_commentinfo[b][0] = n_commentinfo;
                  ary_commentinfo[b][1] = n_commentinfo;
                } else if (ary_commentinfo[b][1] === undefined) {
                  let n_commentinfo = {
                    user_nick: " ",
                    content: " ",
                    image: " "
                  };
                  ary_commentinfo[b][1] = n_commentinfo;
                } else {};
              }
              for (var d in getallinfo) {
                let joininfo = {
                  postinfo: getallinfo[d],
                  commentinfo: ary_commentinfo[d]
                };
                ary_allinfo[d] = joininfo;
              }
              res.status(203).send({
                result: ary_allinfo,
                message: 'ok'
              });
            });

          });
          connection.release();
        });
      });
    }
  });
});

// 카테고리별 인기순 보기 100%
router.get('/categorypopular/:user_nick/:category', function(req, res) {
  pool.getConnection((err, connection) => {
    if (err) res.status(500).send({
      message: ' getConnection err : ' + err
    });
    else {
      let category = req.params.category;
      let usernick = req.params.user_nick;
      let query = 'select User.level, User.nickname, User.profile, Post.id, Post.title, Post.contents, Post.likecount, Post.written_time, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5 from User, Post where User.nickname = Post.user_nick and Post.category = ? order by Post.likecount desc';
      let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
      let query3 = 'select post_id from FavoritePost where user_nick = ? ';
      let query4 = 'select post_id, user_nick, content, image from Comment order by id desc';
      connection.query(query, category, function(err, data) {
        let ary_postinfo = [];
        for (var a in data) {
          let postinfo = {
            level: data[a].level,
            nickname: data[a].nickname,
            profile: data[a].profile,
            id: data[a].id,
            user_nick: data[a].user_nick,
            title: data[a].title,
            contents: data[a].contents,
            likecount: data[a].likecount,
            written_time: data[a].written_time,
            image0: data[a].image1,
            image1: data[a].image2,
            image2: data[a].image3,
            image3: data[a].image4,
            image4: data[a].image5
          };
          ary_postinfo.push(postinfo);
        }
        connection.query(query2, [usernick, ary_postinfo], function(err, data2) {
          let likeunlike = [];
          let allinfo = [];
          for (var b in ary_postinfo) {
            likeunlike[b] = 0;
            for (var c in data2) {
              if (ary_postinfo[b].id === data2[c].post_id) {
                likeunlike[b] = 1;
              }
            }
          }
          for (var c in ary_postinfo) {
            let info = {
              level: ary_postinfo[c].level,
              nickname: ary_postinfo[c].nickname,
              profile: ary_postinfo[c].profile,
              id: ary_postinfo[c].id,
              user_nick: ary_postinfo[c].user_nick,
              title: ary_postinfo[c].title,
              contents: ary_postinfo[c].contents,
              likecount: ary_postinfo[c].likecount,
              written_time: ary_postinfo[c].written_time,
              image0: ary_postinfo[c].image0,
              image1: ary_postinfo[c].image1,
              image2: ary_postinfo[c].image2,
              image3: ary_postinfo[c].image3,
              image4: ary_postinfo[c].image4,
              likecheck: likeunlike[c]
            };
            allinfo.push(info);
          }
          connection.query(query3, [usernick, allinfo], function(err, data3) {
            let markunmark = [];
            let getallinfo = [];
            for (var b in allinfo) {
              markunmark[b] = 0;
              for (var c in data3) {
                if (allinfo[b].id === data3[c].post_id) {
                  markunmark[b] = 1;
                }
              }
            }
            for (var c in allinfo) {
              let info = {
                level: allinfo[c].level,
                nickname: allinfo[c].nickname,
                profile: allinfo[c].profile,
                id: allinfo[c].id,
                user_nick: allinfo[c].user_nick,
                title: allinfo[c].title,
                contents: allinfo[c].contents,
                likecount: allinfo[c].likecount,
                written_time: allinfo[c].written_time,
                image0: allinfo[c].image0,
                image1: allinfo[c].image1,
                image2: allinfo[c].image2,
                image3: allinfo[c].image3,
                image4: allinfo[c].image4,
                likecheck: allinfo[c].likecheck,
                markcheck: markunmark[c]
              };
              getallinfo.push(info);
            }
            connection.query(query4, getallinfo, function(err, data4) {
              let ary_allinfo = new Array();
              let ary_commentinfo = new Array(new Array(), new Array(2));
              let count;
              for (var b in getallinfo) {
                count = 0;
                for (let c = 0; c < data4.length; c++) {
                  if (count !== 2 && getallinfo[b].id === data4[c].post_id) {
                    let commentinfo = {
                      user_nick: data4[c].user_nick,
                      content: data4[c].content,
                      image: data4[c].image
                    };
                    ary_commentinfo[b][count] = commentinfo;
                    count++;
                  };
                }
                if (ary_commentinfo[b][0] === undefined) {
                  let n_commentinfo = {
                    user_nick: " ",
                    content: " ",
                    image: " "
                  };
                  ary_commentinfo[b][0] = n_commentinfo;
                  ary_commentinfo[b][1] = n_commentinfo;
                } else if (ary_commentinfo[b][1] === undefined) {
                  let n_commentinfo = {
                    user_nick: " ",
                    content: " ",
                    image: " "
                  };
                  ary_commentinfo[b][1] = n_commentinfo;
                } else {};
              }
              for (var d in getallinfo) {
                let joininfo = {
                  postinfo: getallinfo[d],
                  commentinfo: ary_commentinfo[d]
                };
                ary_allinfo[d] = joininfo;
              }
              res.status(203).send({
                result: ary_allinfo,
                message: 'ok'
              });
            });

          });
          connection.release();
        });
      });
    }
  });
});

// 좋아요 누를 시
router.post('/postlike/:user_nick/:post_id', (req, res) => {
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
      return new Promise((fulfill, reject) => {
        let query = 'select count(*) as count from PostLikeCount where user_nick=? and post_id=?';
        connection.query(query, [req.params.user_nick, req.params.post_id], (err, data) => {
          if (err) res.status(500).send({
            result: [],
            message: 'first error: ' + err
          });

          else {
            if (data[0].count == 0) { // 좋아요!!!!!!!!!!!!!!!!!!!!!!!!!!!!
              let query2 = 'insert into PostLikeCount set ?';
              let record = {
                user_nick: req.params.user_nick,
                post_id: req.params.post_id
              };
              connection.query(query2, record, (err, data) => {
                if (err) res.status(500).send({
                  result: [],
                  message: 'second error: ' + err
                });
                else {
                  let query3 = 'update Post set likecount=likecount+1 where id=?';
                  connection.query(query3, [req.params.post_id], (err, data) => {
                    if (err) res.status(500).send({
                      result: [],
                      message: 'third error: ' + err
                    });
                    else res.status(200).send({
                      result: data,
                      message: 'ok'
                    });
                    connection.release();
                  });
                }
              });
            } else { // 안좋아요!!!!!!!!!!!!!!!!!!!!!!!!!!!!
              let query4 = 'delete from PostLikeCount where user_nick=? and post_id=?';
              connection.query(query4, [req.params.user_nick, req.params.post_id], (err, data) => {
                if (err) res.status(500).send({
                  result: [],
                  message: 'fourth error: ' + err
                });
                else {
                  let query5 = 'update Post set likecount=likecount-1 where id=?';
                  connection.query(query5, [req.params.post_id], (err, data) => {
                    if (err) res.status(500).send({
                      result: [],
                      message: 'fifth error: ' + err
                    });
                    else res.status(200).send({
                      result: data,
                      message: 'ok'
                    });
                    connection.release();
                  });
                }
              });
            }
          }
        });

      });
    })
});

//게시글 찜하기 100%
router.post('/favoritepost', (req, res) => {
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
      return new Promise((fulfill, reject) => {
        let usernick = req.body.user_nick;
        let postid = req.body.post_id;
        let query = 'select user_nick from FavoritePost where user_nick = ? and post_id = ? ';
        connection.query(query, [usernick, postid], (err, data) => {
          if (err) reject([err, connection]);
          else fulfill([data, usernick, postid, connection]);
        });
      });
    })
    .catch(values => {
      res.status(403).send({
        message: ' err : ' + values[0]
      });
      values[1].release();
    })
    .then(values => {
      return new Promise((reject, fulfill) => {
        let usernick = values[1];
        let postid = values[2];
        console.log(values[0][0]);
        console.log(usernick);
        console.log(postid);
        if (values[0][0] === undefined) {
          console.log(usernick);
          console.log(postid);
          let query2 = 'insert into FavoritePost set ? ';
          let record = {
            user_nick: usernick,
            post_id: postid
          };
          values[3].query(query2, record, function(err, data) {
            console.log(data);
            if (err) res.status(500).send({
              message: 'fail'
            });
            else res.status(203).send({
              message: 'ok'
            });
            values[3].release();
          });
        } else {
          let query3 = 'delete from FavoritePost where user_nick = ? and post_id = ?';
          values[3].query(query3, [usernick, postid], function(err, data) {
            if (err) res.status(500).send({
              message: 'fail'
            });
            else res.status(203).send({
              message: 'ok'
            });
            values[3].release();
          });
        }
      });
    });
});

// 게시글 작성 화면 100%
router.get('/pwrite/:user_nick', (req, res) => {
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
      return new Promise((fulfill, reject) => {
        let query = 'select User.profile,User.level,User.nickname,User.part from User where User.nickname=?';
        //user_nick_temp = req.params.user_nick;
        connection.query(query, [req.params.user_nick], (err, data) => {
          if (err) res.status(500).send({
            result: [],
            message: 'selecting user error: ' + err
          });
          else res.status(200).send({
            result: data,
            message: 'ok'
          });
          //else res.status(200).send({data});

          connection.release();
        });
      });
    })
});

// 글 작성한 뒤 '게시하기'눌렀을 시 [ 확인귀찮.. API짤 때 DB재대로 구성하고 다시 확인 ]
router.post('/add/:user_nick', (req, res) => {
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
      return new Promise((fulfill, reject) => {
        let query = 'insert into Post set ?';
        let record = {
          category: req.body.category,
          part: req.body.part,
          title: req.body.title,
          contents: req.body.contents,
          image1: req.body.image1,
          image2: req.body.image2,
          image3: req.body.image3,
          image4: req.body.image4,
          image5: req.body.image5,
          user_nick: req.params.user_nick,
          written_time: moment(new Date()).format('YYYY-MM-DD')
        };
        connection.query(query, record, (err, data) => {
          if (err) res.status(500).send({
            result: [],
            message: 'selecting user error: ' + err
          });
          else res.status(200).send({
            result: data,
            message: 'ok'
          });
          //else res.status(200).send({data});

          connection.release();
        });
      });
    })
});

//게시글 자세히 보기
router.get('/post/:post_id', (req, res) => {
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
      return new Promise((fulfill, reject) => {
        let postid = req.params.post_id;
        let query = ' select User.nickname, User.level, User.part as userpart , User.profile, Post.title, Post.contents, Post.written_time, Post.part as postpart, Post.category, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5, Post.likecount, Post.commentcount from User, Post where Post.id = ? ';
        let query2 = ' select user_nick, content, image, written_time from Comment where post_id = ? order by id desc';
        connection.query(query, postid, (err, data) => {
          let postinfo = {
            nickname: data[0].nickname,
            level: data[0].level,
            userpart: data[0].userpart,
            profile: data[0].profile,
            title: data[0].title,
            contents: data[0].contents,
            written_time: data[0].written_time,
            postpart: data[0].postpart,
            category: data[0].category,
            image0: data[0].image1,
            image1: data[0].image2,
            image2: data[0].image3,
            image3: data[0].image4,
            image4: data[0].image5,
            likecount: data[0].likecount,
            commentcount: data[0].commentcount
          };
          connection.query(query2, [postid, postinfo], (err, data2) => {
            if (err) res.status(500).send({
              message: 'fail'
            });
            else {
              let com = [data2[0], data2[1]];
              let ary_all = [postinfo, com];
              res.status(200).send({
                result: ary_all,
                message: 'success'
              });
            }
            connection.release();
          });
        });
      });
    });
});

// 검색 돋보기 버튼 눌렀을 시
router.post('/find', (req, res) => {
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
      return new Promise((fulfill, reject) => {
        //let query = 'select post.user_nick,post.title from post where post.title LIKE "%"?"%"';
        //let userid = req.params.user_nick;
        let query = 'select user_nick, title from Post where title LIKE "%"?"%" and part=?';
        connection.query(query, [req.body.search_content, req.body.part], (err, data) => {
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

//게시글 삭제 100%
router.post('/delete', (req, res) => {
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
      return new Promise((fulfill, reject) => {
        let postid = req.body.post_id;
        let usernick = req.body.user_nick;
        let query = 'delete from Post where id = ? and user_nick = ? ';
        connection.query(query, [postid, usernick], (err, data) => {
          if (err) res.status(500).send({
            message: ' query error : ' + err
          });
          else {
            res.status(203).send({
              message: ' delete success '
            });
          }
        });
      });
    });
});


module.exports = router;
