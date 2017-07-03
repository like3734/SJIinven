
// 메인 list 띄워주기 + 최신순
router.get('/partlatest/:user_nick/:part', function(req, res) {
  pool.getConnection((err, connection) => {
    if (err) res.status(500).send({
      message: ' getConnection err : ' + err
    });
    else {
      let userpart = req.params.part;
      let usernick = req.params.user_nick;
      let query = 'select User.part, User.level, User.nickname, User.profile, User.statemessage, Post.id, Post.title, Post.contents, Post.likecount, Post.commentcount, Post.written_time, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5 from User, Post where User.nickname = Post.user_nick and Post.part = ? order by Post.id desc';
      let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
      let query3 = 'select post_id from FavoritePost where user_nick = ? ';
      let query4 = 'select Comment.post_id, Comment.user_nick, Comment.content, Comment.written_time, User.profile, User.level, User.statemessage from User, Comment where User.nickname = Comment.user_nick order by Comment.id desc';
      connection.query(query, userpart, function(err, data) {
        if(err) res.status(500).send({ result: [], message: 'query error : ' + err});
        else{
        let ary_postinfo = [];
        for (var a in data) {
          let postinfo = {
            part: data[a].part,
            level: data[a].level,
            nickname: data[a].nickname,
            profile: data[a].profile,
            statemessage: data[a].statemessage,
            id: data[a].id,
            user_nick: data[a].user_nick,
            title: data[a].title,
            contents: data[a].contents,
            likecount: data[a].likecount,
            commentcount: data[a].commentcount,
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
          if(err) res.status(500).send({ result: [], message: 'query2 error : ' + err});
          else{
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
              part: ary_postinfo[c].part,
              level: ary_postinfo[c].level,
              nickname: ary_postinfo[c].nickname,
              profile: ary_postinfo[c].profile,
              statemessage: ary_postinfo[c].statemessage,
              id: ary_postinfo[c].id,
              user_nick: ary_postinfo[c].user_nick,
              title: ary_postinfo[c].title,
              contents: ary_postinfo[c].contents,
              likecount: ary_postinfo[c].likecount,
              commentcount: ary_postinfo[c].commentcount,
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
            if(err) res.status(500).send({ result: [], message: 'query3 error : ' + err});
            else{
            let markunmark = [];
            let getallinfo = [];
            let ary_image = [];
            for(let idx=0; idx<allinfo.length; idx++){
              ary_image[idx] = new Array();
            }
            for (var b in allinfo) {
              markunmark[b] = 0;
              for (var c in data3) {
                if (allinfo[b].id === data3[c].post_id) {
                  markunmark[b] = 1;
                }
              }
            }
            for (var c in allinfo) {
              ary_image[c].push(allinfo[c].image0);
              ary_image[c].push(allinfo[c].image1);
              ary_image[c].push(allinfo[c].image2);
              ary_image[c].push(allinfo[c].image3);
              ary_image[c].push(allinfo[c].image4);
            }
            for( var e in allinfo) {
              let info = {
                part: allinfo[e].part,
                level: allinfo[e].level,
                nickname: allinfo[e].nickname,
                profile: allinfo[e].profile,
                statemessage: allinfo[e].statemessage,
                id: allinfo[e].id,
                user_nick: allinfo[e].user_nick,
                title: allinfo[e].title,
                contents: allinfo[e].contents,
                likecount: allinfo[e].likecount,
                commentcount: allinfo[e].commentcount,
                written_time: allinfo[e].written_time,
                likecheck: allinfo[e].likecheck,
                markcheck: markunmark[e],
                image: ary_image[e]
              };
              getallinfo.push(info);

            }
            connection.query(query4, getallinfo, function(err, data4) {
              if(err) res.status(500).send({ result: [], message: ' query4 error : ' + err});
              else
              {
              ary_allinfo = [];
              let arylength = getallinfo.length;
              let ary_commentinfo = [];
              for(let idx=0; idx<arylength; idx++){
                ary_commentinfo[idx] = new Array();
              }
              let count;
              for (var b in getallinfo) {
                count = 0;
                for (let c = 0; c < data4.length; c++) {
                  if (count !== 2 && getallinfo[b].id === data4[c].post_id) {
                    let commentinfo = {
                      user_nick: data4[c].user_nick,
                      content: data4[c].content,
                      image: data4[c].profile,
                      level: data4[c].level,
                      written_time: data4[c].written_time,
                      statemessage: data4[c].statemessage
                    };
                    ary_commentinfo[b][count] = commentinfo;
                    count++;
                  };
                }
              }
              for(var d in getallinfo) {
                let allinfo = {
                  postinfo: getallinfo[d],
                  commentinfo: ary_commentinfo[d]
                };
                ary_allinfo[d] = allinfo;
              }
              res.status(203).send({
                result: ary_allinfo,
                message: 'ok'
              });
            }
            connection.release();
            }); // query4 connection
          }
          }); // query3 connection
        }
        }); // query2 connection
      }
      }); // query connection
    } // else 1
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
      let query = 'select User.part, User.level, User.nickname, User.profile, User.statemessage, Post.id, Post.title, Post.contents, Post.likecount, Post.commentcount, Post.written_time, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5 from User, Post where User.nickname = Post.user_nick and Post.part = ? order by Post.likecount desc';
      let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
      let query3 = 'select post_id from FavoritePost where user_nick = ? ';
      let query4 = 'select Comment.post_id, Comment.user_nick, Comment.content, Comment.written_time, User.profile, User.level, User.statemessage from User, Comment where User.nickname = Comment.user_nick order by Comment.id desc';
      connection.query(query, userpart, function(err, data) {
        if(err) res.status(500).send({ result: [], message: 'query error : ' + err});
        else{
        let ary_postinfo = [];
        for (var a in data) {
          let postinfo = {
            part: data[a].part,
            level: data[a].level,
            nickname: data[a].nickname,
            profile: data[a].profile,
            statemessage: data[a].statemessage,
            id: data[a].id,
            user_nick: data[a].user_nick,
            title: data[a].title,
            contents: data[a].contents,
            likecount: data[a].likecount,
            commentcount: data[a].commentcount,
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
          if(err) res.status(500).send({ result: [], message: 'query2 error : ' + err});
          else{
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
              part: ary_postinfo[c].part,
              level: ary_postinfo[c].level,
              nickname: ary_postinfo[c].nickname,
              profile: ary_postinfo[c].profile,
              statemessage: ary_postinfo[c].statemessage,
              id: ary_postinfo[c].id,
              user_nick: ary_postinfo[c].user_nick,
              title: ary_postinfo[c].title,
              contents: ary_postinfo[c].contents,
              likecount: ary_postinfo[c].likecount,
              commentcount: ary_postinfo[c].commentcount,
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
            if(err) res.status(500).send({ result: [], message: 'query3 error : ' + err});
            else{
            let markunmark = [];
            let getallinfo = [];
            let ary_image = [];
            for(let idx=0; idx<allinfo.length; idx++){
              ary_image[idx] = new Array();
            }
            for (var b in allinfo) {
              markunmark[b] = 0;
              for (var c in data3) {
                if (allinfo[b].id === data3[c].post_id) {
                  markunmark[b] = 1;
                }
              }
            }
            for (var c in allinfo) {
              ary_image[c].push(allinfo[c].image0);
              ary_image[c].push(allinfo[c].image1);
              ary_image[c].push(allinfo[c].image2);
              ary_image[c].push(allinfo[c].image3);
              ary_image[c].push(allinfo[c].image4);
            }
            for( var e in allinfo) {
              let info = {
                part: allinfo[e].part,
                level: allinfo[e].level,
                nickname: allinfo[e].nickname,
                profile: allinfo[e].profile,
                statemessage: allinfo[e].statemessage,
                id: allinfo[e].id,
                user_nick: allinfo[e].user_nick,
                title: allinfo[e].title,
                contents: allinfo[e].contents,
                likecount: allinfo[e].likecount,
                commentcount: allinfo[e].commentcount,
                written_time: allinfo[e].written_time,
                likecheck: allinfo[e].likecheck,
                markcheck: markunmark[e],
                image: ary_image[e]
              };
              getallinfo.push(info);

            }
            connection.query(query4, getallinfo, function(err, data4) {
              if(err) res.status(500).send({ result: [], message: 'query4 error : ' + err});
              else{
              //let ary_allinfo = new Array();
              ary_allinfo = [];
              let arylength = getallinfo.length;
              let ary_commentinfo = [];
              for(let idx=0; idx<arylength; idx++){
                ary_commentinfo[idx] = new Array();
              }
              let count;
              for (var b in getallinfo) {
                count = 0;
                for (let c = 0; c < data4.length; c++) {
                  if (count !== 2 && getallinfo[b].id === data4[c].post_id) {
                    let commentinfo = {
                      user_nick: data4[c].user_nick,
                      content: data4[c].content,
                      image: data4[c].profile,
                      level: data4[c].level,
                      written_time: data4[c].written_time,
                      statemessage: data4[c].statemessage
                    };
                    ary_commentinfo[b][count] = commentinfo;
                    count++;
                  };
                }
              }
              for(var d in getallinfo) {
                let allinfo = {
                  postinfo: getallinfo[d],
                  commentinfo: ary_commentinfo[d]
                };
                ary_allinfo[d] = allinfo;
              }
              res.status(203).send({
                result: ary_allinfo,
                message: 'ok'
              });
            }
            connection.release();
            });
          }
          });
        }
        });
      }
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
      let query = 'select User.part, User.level, User.nickname, User.profile, User.statemessage, Post.id, Post.title, Post.contents, Post.likecount, Post.commentcount, Post.written_time, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5 from User, Post where User.nickname = Post.user_nick and Post.category = ? order by Post.id desc';
      let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
      let query3 = 'select post_id from FavoritePost where user_nick = ? ';
      let query4 = 'select Comment.post_id, Comment.user_nick, Comment.content, Comment.written_time, User.profile, User.level, User.statemessage from User, Comment where User.nickname = Comment.user_nick order by Comment.id desc';
      connection.query(query, category, function(err, data) {
        if(err) res.status(500).send({ result: [], message: 'query error : ' + err});
        else{
        let ary_postinfo = [];
        for (var a in data) {
          let postinfo = {
            part: data[a].part,
            level: data[a].level,
            nickname: data[a].nickname,
            profile: data[a].profile,
            statemessage: data[a].statemessage,
            id: data[a].id,
            user_nick: data[a].user_nick,
            title: data[a].title,
            contents: data[a].contents,
            likecount: data[a].likecount,
            commentcount: data[a].commentcount,
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
          if(err) res.status(500).send({ result: [], message: 'query2 error : ' + err});
          else{
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
              part: ary_postinfo[c].part,
              level: ary_postinfo[c].level,
              nickname: ary_postinfo[c].nickname,
              profile: ary_postinfo[c].profile,
              statemessage: ary_postinfo[c].statemessage,
              id: ary_postinfo[c].id,
              user_nick: ary_postinfo[c].user_nick,
              title: ary_postinfo[c].title,
              contents: ary_postinfo[c].contents,
              likecount: ary_postinfo[c].likecount,
              commentcount: ary_postinfo[c].commentcount,
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
            if(err) res.status(500).send({ result: [], message: 'query3 error : ' + err});
            else{
            let markunmark = [];
            let getallinfo = [];
            let ary_image = [];
            for(let idx=0; idx<allinfo.length; idx++){
              ary_image[idx] = new Array();
            }
            for (var b in allinfo) {
              markunmark[b] = 0;
              for (var c in data3) {
                if (allinfo[b].id === data3[c].post_id) {
                  markunmark[b] = 1;
                }
              }
            }
            for (var c in allinfo) {
              ary_image[c].push(allinfo[c].image0);
              ary_image[c].push(allinfo[c].image1);
              ary_image[c].push(allinfo[c].image2);
              ary_image[c].push(allinfo[c].image3);
              ary_image[c].push(allinfo[c].image4);
            }
            for( var e in allinfo) {
              let info = {
                part: allinfo[e].part,
                level: allinfo[e].level,
                nickname: allinfo[e].nickname,
                profile: allinfo[e].profile,
                statemessage: allinfo[e].statemessage,
                id: allinfo[e].id,
                user_nick: allinfo[e].user_nick,
                title: allinfo[e].title,
                contents: allinfo[e].contents,
                likecount: allinfo[e].likecount,
                commentcount: allinfo[e].commentcount,
                written_time: allinfo[e].written_time,
                likecheck: allinfo[e].likecheck,
                markcheck: markunmark[e],
                image: ary_image[e]
              };
              getallinfo.push(info);

            }
            connection.query(query4, getallinfo, function(err, data4) {
              if(err) res.status(500).send({ result: [], message: 'query4 error : ' + err});
              else{
              //let ary_allinfo = new Array();
              ary_allinfo = [];
              let arylength = getallinfo.length;
              let ary_commentinfo = [];
              for(let idx=0; idx<arylength; idx++){
                ary_commentinfo[idx] = new Array();
              }
              let count;
              for (var b in getallinfo) {
                count = 0;
                for (let c = 0; c < data4.length; c++) {
                  if (count !== 2 && getallinfo[b].id === data4[c].post_id) {
                    let commentinfo = {
                      user_nick: data4[c].user_nick,
                      content: data4[c].content,
                      image: data4[c].profile,
                      level: data4[c].level,
                      written_time: data4[c].written_time,
                      statemessage: data4[c].statemessage
                    };
                    ary_commentinfo[b][count] = commentinfo;
                    count++;
                  };
                }
              }
              for(var d in getallinfo) {
                let allinfo = {
                  postinfo: getallinfo[d],
                  commentinfo: ary_commentinfo[d]
                };
                ary_allinfo[d] = allinfo;
              }
              res.status(203).send({
                result: ary_allinfo,
                message: 'ok'
              });
            }
            connection.release();
            });
          }
          });
        }
        });
      }
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
      let query = 'select User.part, User.level, User.nickname, User.profile, User.statemessage, Post.id, Post.title, Post.contents, Post.likecount, Post.commentcount, Post.written_time, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5 from User, Post where User.nickname = Post.user_nick and Post.category = ? order by Post.likecount desc';
      let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
      let query3 = 'select post_id from FavoritePost where user_nick = ? ';
      let query4 = 'select Comment.post_id, Comment.user_nick, Comment.content, Comment.written_time, User.profile, User.level, User.statemessage from User, Comment where User.nickname = Comment.user_nick order by Comment.id desc';
      connection.query(query, category, function(err, data) {
        if(err) res.status(500).send({ result: [], message: 'query4 error : ' + err});
        else{
        let ary_postinfo = [];
        for (var a in data) {
          let postinfo = {
            part: data[a].part,
            level: data[a].level,
            nickname: data[a].nickname,
            profile: data[a].profile,
            statemessage: data[a].statemessage,
            id: data[a].id,
            user_nick: data[a].user_nick,
            title: data[a].title,
            contents: data[a].contents,
            likecount: data[a].likecount,
            commentcount: data[a].commentcount,
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
          if(err) res.status(500).send({ result: [], message: 'query2 error : ' + err});
          else{
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
              part: ary_postinfo[c].part,
              level: ary_postinfo[c].level,
              nickname: ary_postinfo[c].nickname,
              profile: ary_postinfo[c].profile,
              statemessage: ary_postinfo[c].statemessage,
              id: ary_postinfo[c].id,
              user_nick: ary_postinfo[c].user_nick,
              title: ary_postinfo[c].title,
              contents: ary_postinfo[c].contents,
              likecount: ary_postinfo[c].likecount,
              commentcount: ary_postinfo[c].commentcount,
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
            if(err) res.status(500).send({ result: [], message: 'query3 error : ' + err});
            else{
            let markunmark = [];
            let getallinfo = [];
            let ary_image = [];
            for(let idx=0; idx<allinfo.length; idx++){
              ary_image[idx] = new Array();
            }
            for (var b in allinfo) {
              markunmark[b] = 0;
              for (var c in data3) {
                if (allinfo[b].id === data3[c].post_id) {
                  markunmark[b] = 1;
                }
              }
            }
            for (var c in allinfo) {
              ary_image[c].push(allinfo[c].image0);
              ary_image[c].push(allinfo[c].image1);
              ary_image[c].push(allinfo[c].image2);
              ary_image[c].push(allinfo[c].image3);
              ary_image[c].push(allinfo[c].image4);
            }
            for( var e in allinfo) {
              let info = {
                part: allinfo[e].part,
                level: allinfo[e].level,
                nickname: allinfo[e].nickname,
                profile: allinfo[e].profile,
                statemessage: allinfo[e].statemessage,
                id: allinfo[e].id,
                user_nick: allinfo[e].user_nick,
                title: allinfo[e].title,
                contents: allinfo[e].contents,
                likecount: allinfo[e].likecount,
                commentcount: allinfo[e].commentcount,
                written_time: allinfo[e].written_time,
                likecheck: allinfo[e].likecheck,
                markcheck: markunmark[e],
                image: ary_image[e]
              };
              getallinfo.push(info);

            }
            connection.query(query4, getallinfo, function(err, data4) {
              if(err) res.status(500).send({ result: [], message: 'query4 error : ' + err});
              else{
              //let ary_allinfo = new Array();
              ary_allinfo = [];
              let arylength = getallinfo.length;
              let ary_commentinfo = [];
              for(let idx=0; idx<arylength; idx++){
                ary_commentinfo[idx] = new Array();
              }
              let count;
              for (var b in getallinfo) {
                count = 0;
                for (let c = 0; c < data4.length; c++) {
                  if (count !== 2 && getallinfo[b].id === data4[c].post_id) {
                    let commentinfo = {
                      user_nick: data4[c].user_nick,
                      content: data4[c].content,
                      image: data4[c].profile,
                      level: data4[c].level,
                      written_time: data4[c].written_time,
                      statemessage: data4[c].statemessage
                    };
                    ary_commentinfo[b][count] = commentinfo;
                    count++;
                  };
                }
              }
              for(var d in getallinfo) {
                let allinfo = {
                  postinfo: getallinfo[d],
                  commentinfo: ary_commentinfo[d]
                };
                ary_allinfo[d] = allinfo;
              }
              res.status(203).send({
                result: ary_allinfo,
                message: 'ok'
              });
            }
            connection.release();
            });
          }
          });
        }
        });
      }
      });
    }
  });
});

// 좋아요 누를 시
router.get('/postlike/:user_nick/:post_id', (req, res) => {
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
        let query = 'select count(*) as count from PostLikeCount where user_nick=? and post_id=?';
        connection.query(query, [req.params.user_nick, req.params.post_id], (err, data) => {
          if (err) res.status(500).send({ result: [], message: 'first error: ' + err });
          else {
            if (data[0].count == 0) { // 좋아요!!!!!!!!!!!!!!!!!!!!!!!!!!!!
              let query2 = 'insert into PostLikeCount set ?';
              let record = {
                user_nick: req.params.user_nick,
                post_id: req.params.post_id
              };
              connection.query(query2, record, (err, data) => {
                if (err) res.status(500).send({ result: [], message: 'second error: ' + err });
                else {
                  let query3 = 'update Post set likecount=likecount+1 where id=?';
                  connection.query(query3, [req.params.post_id], (err, data) => {
                    if (err) res.status(500).send({ result: [], message: 'third error: ' + err });
                    else {
                      let query4 = 'select likecount from Post where id = ? ';
                      connection.query(query4, [req.params.post_id], (err, data) =>{
                        if(err) res.status(500).send({ result: [], message: ' select likecount err : ' + err });
                        else{
                            res.status(200).send({ result: data, message: 'like' });
                        }
                        connection.release();
                      });
                    }
                  });
                }
              });
            }
            else { // 안좋아요!!!!!!!!!!!!!!!!!!!!!!!!!!!!
              let query5 = 'delete from PostLikeCount where user_nick=? and post_id=?';
              connection.query(query5, [req.params.user_nick, req.params.post_id], (err, data) => {
                if (err) res.status(500).send({ result: [], message: 'fourth error: ' + err });
                else {
                  let query6 = 'update Post set likecount=likecount-1 where id=?';
                  connection.query(query6, [req.params.post_id], (err, data) => {
                    if (err) res.status(500).send({ result: [], message: 'fifth error: ' + err });
                    else {
                      let query7 = 'select likecount from Post where id = ? ';
                      connection.query(query7, [req.params.post_id], (err, data) =>{
                        if(err) res.status(500).send({ result: [], message: ' select likecount err : ' + err });
                        else{
                            res.status(200).send({ result: data, message: 'unlike' });
                        }
                        connection.release();
                      });
                    }
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
router.get('/bookmark/:user_nick/:post_id', (req, res) => {
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
        let usernick = req.params.user_nick;
        let postid = req.params.post_id;
        let query = 'select count(*) as fcount from FavoritePost where user_nick = ? and post_id = ? ';
        connection.query(query, [usernick, postid], (err, data) => {
          if (err) reject([err, connection]);
          else fulfill([data, usernick, postid, connection]);
        });
      });
    })
    .catch(values => {
      res.status(403).send({ message: ' err : ' + values[0] });
      values[1].release();
    })
    .then(values => {
      return new Promise((reject, fulfill) => {
        let usernick = values[1];
        let postid = values[2];
        if (values[0][0].fcount === 0 ) {
          let query2 = 'insert into FavoritePost set ? ';
          let record = {
            user_nick: usernick,
            post_id: postid
          };
          values[3].query(query2, record, function(err, data) {
            if (err) res.status(500).send({ message: 'fail' });
            else res.status(203).send({ message: 'mark' });
            values[3].release();
          });
        } else {
          let query3 = 'delete from FavoritePost where user_nick = ? and post_id = ?';
          values[3].query(query3, [usernick, postid], function(err, data) {
            if (err) res.status(500).send({ message: 'fail' });
            else res.status(203).send({ message: 'unmark' });
            values[3].release();
          });
        }
      });
    });
});

// 게시글 작성 화면 100%
router.get('/write/:user_nick', (req, res) => {
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
        let query = 'select User.profile,User.level,User.nickname,User.part from User where User.nickname=?';
        connection.query(query, [req.params.user_nick], (err, data) => {
          if (err) res.status(500).send({ result: [], message: 'selecting user error: ' + err });
          else res.status(200).send({
            result: data,
            message: 'ok'
          });
          connection.release();
        });
      });
    })
});

// 글 작성한 뒤 '게시하기'눌렀을 시
router.post('/add', upload.array('image', 5), (req, res) => {
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
        let query = 'insert into Post set ?';
        //console.log(req.files);
        let record = {
          category: req.body.category,
          part: req.body.part,
          title: req.body.title,
          contents: req.body.contents,
          image1: req.files[0] ? req.files[0].location : null,
          image2: req.files[1] ? req.files[1].location : null,
          image3: req.files[2] ? req.files[2].location : null,
          image4: req.files[3] ? req.files[3].location : null,
          image5: req.files[4] ? req.files[4].location : null,
          user_nick: req.body.user_nick,
          written_time: moment(new Date()).format('YYYY-MM-DD')
        };
        connection.query(query, record, (err, data) => {
          if (err) res.status(500).send({ message: 'selecting user error: ' + err  });
          else res.status(200).send({ message: 'ok' });
          connection.release();
        });
      });
    })
});


// 게시글 자세히 보기
router.post('/post', (req, res) => {
  pool.getConnection((err, connection) => {
    if (err) res.status(500).send({
      message: ' getConnection err : ' + err
    });
    else{
        let postid = req.body.post_id;
        let nickname = req.body.user_nick;
        let query = ' select User.nickname, User.level, User.part as userpart , User.profile, User.statemessage, Post.title, Post.contents, Post.written_time, Post.part as postpart, Post.category, Post.image1, Post.image2, Post.image3, Post.image4, Post.image5, Post.likecount, Post.commentcount from User, Post where User.nickname = Post.user_nick and Post.id = ? ';
        let query2 = 'select post_id from PostLikeCount where user_nick = ? ';
        let query3 = 'select post_id from FavoritePost where user_nick = ? ';
        let query4 = ' select Comment.user_nick, Comment.content, User.profile, Comment.written_time, User.level, User.part, User.statemessage from Comment, User where post_id = ? and Comment.user_nick = User.nickname order by Comment.id desc';
        connection.query(query, postid, (err, data)=> {
          if(err) res.status(500).send({ result: [], message: 'query error : ' + err});
          else{
          connection.query(query2, [nickname, data], (err, data2) => {
            if(err) res.status(500).send({ result: [], message: 'query2 error : ' + err});
            else{
            let likecheck = 0;
            for(let a=0; a < data2.length ; a++){
              let id = data2[a].post_id;
              if(postid == data2[a].post_id){
                likecheck = 1;
              }
            }
            connection.query(query3, [nickname, data, likecheck], (err, data3) => {
              if(err) res.status(500).send({ result: [], message: 'query3 error : ' + err});
              else{
                ary_image = [];
                let markcheck = 0;
                for(let a=0; a< data3.length ; a++){
                  let id = data3[a].post_id;
                  if(postid == data3[a].post_id){
                    markcheck = 1;
                  }
                }
                ary_image.push(data[0].image1);
                ary_image.push(data[0].image2);
                ary_image.push(data[0].image3);
                ary_image.push(data[0].image4);
                ary_image.push(data[0].image5);

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
                connection.query(query4, [postid, postinfo], (err, data4) => {
                  if (err) res.status(500).send({ result: [], message: 'fail' });
                  else {
                    let com = [data4[0], data4[1]];
                    let ary_all = { postinpo: postinfo, commentinfo: com };
                    res.status(200).send({
                      result: ary_all,
                      message: 'success'
                    });
                  }
                  connection.release();
                });
              }
            });
            }
          });
        }
        });
    }
  })
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
      res.status(500).send({ result: [], message: 'getConnection error : ' + err });
    })
    .then(connection => {
      return new Promise((fulfill, reject) => {
        let query = 'select user_nick, title, id from Post where title LIKE "%"?"%" and part=?';
        connection.query(query, [req.body.search_content, req.body.part], (err, data) => {
          if (err) res.status(500).send({
            result: [],
            message: 'selecting user error: ' + err
          });
          else {
            res.status(200).send({ result: data, message: 'ok' });
          }
          connection.release();
        });
      });
    });
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
      res.status(500).send({ message: 'getConnection error : ' + err });
    })
    .then(connection => {
      return new Promise((fulfill, reject) => {
        let postid = req.body.post_id;
        let usernick = req.body.user_nick;
        let query = 'delete from Post where id = ? and user_nick = ? ';
        connection.query(query, [postid, usernick], (err, data) => {
          if (err) res.status(500).send({ message: ' query error : ' + err });
          else {
            res.status(203).send({ message: 'success'});
          }
          connection.release();
        });
      });
    });
});

// 게시글 삭제 유효성 검사
router.post('/deletecheck', (req, res) => {
  return new Promise((fulfill, reject) => {
    pool.getConnection((err, connection) => {
      if(err) reject(err);
      else fulfill(connection);
    });
  })
  .catch(err => {
    res.status(500).send({ message: 'getConnection err : ' + err});
  })
  .then(connection => {
    return new Promise((fulfill, reject) => {
      let postid = req.body.post_id;
      let usernick = req.body.user_nick;
      let query = 'select count(*) as checking from Post where id = ? and user_nick = ? ';
      connection.query(query, [postid, usernick], (err, data) =>{
        if(err) reject([err, connection]);
        else fulfill([data, connection]);
      });
      connection.release();
    })
    .catch(values => {
      res.status(500).send({ message: ' select error : ' + values[0] });
      values[1].release();
    })
    .then(values =>{
      if(values[0][0].checking === 0){
        res.status(500).send({ message: 'deny' });
      }
      else{
          res.status(203).send({ message: 'ok'});
      }
      values[1].release();
    });
  });
})


module.exports = router;
