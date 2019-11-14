<!DOCTYPE html>
<html>

<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css">
  <link rel="stylesheet" href="/resources/css/croppie.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-route.js"></script>
<script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
<script src="/resources/js/croppie.min.js"></script>

<style>
.ng-hide:not(.ng-hide-animate) {
  display: block!important;
  position: absolute;
  top: -9999px;
  left: -9999px;
}
</style>

<body ng-app="myApp">

<div class="container">
<div class="contain">
<div class="row">
<div class="col-lg-12">
<a href="#/!" class="btn btn-default btn-sm">Home</a>
<a href="#!userlist" class="btn btn-default btn-sm">User list</a>
<a href="#!userAdd" class="btn btn-default btn-sm">User Add</a>
<a href="#!blue" class="btn btn-default btn-sm">Blue</a>
</div>
</div>

<div class="row">
<div class="col-lg-12">
<div ng-view></div>
</div>
</div>
</div>
</div>

<script>
var userListTables;

$(document).ready(function() {



});


var app = angular.module("myApp", ["ngRoute"]);
app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "pages/main.htm"
    })
    .when("/userlist", {
        templateUrl : "pages/userList.htm"
    })
    .when("/userAdd", {
        templateUrl : "pages/userAdd.htm"
    })
    .when("/blue", {
        templateUrl : "pages/blue.htm"
    });
});

app.controller('userCtrl', function($scope, $http , $location , $compile) {

    if(userListTables === undefined){
        userListTables = $('#userListTable').DataTable();
    }

  $http({
    method : "GET",
      url : "/api/user/list"
  }).then(function mySuccess(response) {
    console.log(response.data);
    $scope.userList = response.data;
    $scope.userDataTable(response.data , $scope.userDataTableMaker);
  }, function myError(response) {
    console.log(response.statusText);
  });

  $scope.deleteUser = function(userId) {
  console.log("user id : " + userId);
      $http({
          method : "POST",
            url : "/api/user/delete",
            responseType: 'text',
            data:{id: userId}

        }).then(function mySuccess(response) {
          console.log("success");
          $scope.msg = response.data.msg;
          $scope.userList = response.data.details;
          $scope.userDataTable(response.data.details , $scope.userDataTableMaker);
        }, function myError(response) {
        console.log("error");
        console.log(response)
          $scope.msg = response.data.msg;
        });
    },

    $scope.editUser = function(userId) {
    $scope.msg = "";
    $scope.loadingMsg = "please wait....!!!";
    $scope.username = "";
    $scope.password = "";
    $scope.loadingUserData = "true";

      console.log(userId);
      console.log("user id : " + userId);
          $http({
              method : "POST",
                url : "/api/user/edit",
                responseType: 'text',
                data:{id: userId}

            }).then(function mySuccess(response) {
              $scope.loadingMsg = "";
              $scope.userInfoUpdate = response.data.details;
              $scope.username = response.data.details.username;
              //$scope.password = response.data.details.password;
              $scope.loadingUserData = "";
            }, function myError(response) {
            console.log("error");
            console.log(response)
              $scope.msg = response.data.msg;
            });
        },

    $scope.updateUser = function(user) {
    $scope.loadingMsg = "please wait....!!!";
     $scope.loadingUserData = "true";
          $http({
              method : "POST",
                url : "/api/user/update",
                responseType: 'text',
                data:{id: user.id , username: $scope.username , password : $scope.password}

            }).then(function mySuccess(response) {
              console.log("success");
              $scope.msg = response.data.msg;
              $scope.userList = response.data.details;
              $scope.userDataTable(response.data.details , $scope.userDataTableMaker);
              $scope.loadingMsg = "";
              $scope.loadingUserData = "";
              $("#closeUserEdit").click();
            }, function myError(response) {
            console.log("error");
            console.log(response)
              $scope.msg = response.data.msg;
            });
        },

        $scope.userDataTable = function(userList , callback) {
            var resultList = [];

                var i;
                for (i = 0; i < userList.length; ++i) {
                    var temp = [];
                    temp.push(i + 1);
                    temp.push(userList[i].username);
                    temp.push(userList[i].password);
                    temp.push("<a class='btn btn-danger btn-sm' ng-click='deleteUser("+userList[i].id+")'>delete</a>&nbsp;<button type='button' class='btn btn-primary btn-sm' data-toggle='modal' data-target='#editModal' ng-click='editUser("+userList[i].id+")'>edit</button>");

                    resultList.push(temp);
                    console.log((i + 1) == userList.length);
                    if((i + 1) === userList.length){
                        userListTables.destroy();
                        callback(resultList);
                    }
                }
        },

        $scope.userDataTableMaker = function(userList) {
            console.log("datatable..!!");
           userListTables = $('#userListTable').DataTable( {
                   "data": userList,
                   "fnRowCallback": function( nRow, aData, iDisplayIndex ) {
                        $scope.content = nRow;
                        var tblElem = angular.element($scope.content);
                       var compileFn = $compile(tblElem);
                       compileFn($scope);
                   }
               } );
       }


});



app.controller('userAddCtrl', function($scope , $http , $location) {
  $scope.showVar = "";
  $scope.addVar = "true";

  $scope.saveUser = function() {
    $http({
        method : "POST",
          url : "/api/user/save",
          responseType: 'text',
          data:{username: $scope.username , password : $scope.password}

      }).then(function mySuccess(response) {
        console.log("success");
        $scope.msg = response.data.msg;
        $scope.username = "";
        $scope.password = "";
        console.log(response.data.status)
        if(response.data.status === "200"){
            console.log(response.data.status === "200")
            $scope.userinfo = response.data.details;
            $scope.showVar = "true";
            $scope.addVar = "";
        }

      }, function myError(response) {
      console.log("error");
      console.log(response)
        $scope.msg = response.data.msg;
      });
  },

  $scope.addNewUser = function() {
    $scope.showVar = "";
    $scope.addVar = "true";
    $scope.userinfo.username = "";
    $scope.userinfo.password = "";
    $scope.username = "";
    $scope.password = "";
    $scope.msg = "";
  },

  $scope.deleteUser = function(user) {
    console.log(user);
    console.log("user id : " + user.id);
        $http({
            method : "POST",
              url : "/api/user/delete",
              responseType: 'text',
              data:{id: user.id}

          }).then(function mySuccess(response) {
            console.log("success");
            $scope.msg = response.data.msg;
            //$location.path( "userlist" );
            $scope.showVar = "";
            $scope.addVar = "true";
            $scope.username = "";
            $scope.password = "";
          }, function myError(response) {
          console.log("error");
          console.log(response)
            $scope.msg = response.data.msg;
          });
      },

      $scope.editUser = function(user) {
          $scope.msg = "";
          $scope.loadingMsg = "please wait....!!!";
          $scope.username = "";
          $scope.password = "";
          $scope.loadingUserData = "true";

            console.log(user);
            console.log("user id : " + user.id);
                $http({
                    method : "POST",
                      url : "/api/user/edit",
                      responseType: 'text',
                      data:{id: user.id}

                  }).then(function mySuccess(response) {
                    $scope.loadingMsg = "";
                    $scope.userInfoUpdate = response.data.details;
                    $scope.username = response.data.details.username;
                    //$scope.password = response.data.details.password;
                    $scope.loadingUserData = "";
                  }, function myError(response) {
                  console.log("error");
                  console.log(response)
                    $scope.msg = response.data.msg;
                  });
              },

          $scope.updateUser = function(user) {
          $scope.loadingMsg = "please wait....!!!";
           $scope.loadingUserData = "true";
                $http({
                    method : "POST",
                      url : "/api/user/update",
                      responseType: 'text',
                      data:{id: user.id , username: $scope.username , password : $scope.password}

                  }).then(function mySuccess(response) {
                    console.log("success");
                    $scope.msg = response.data.msg;
                    $scope.userinfo = response.data.info;
                    $scope.showVar = "true";
                    $scope.addVar = "";
                    $scope.loadingMsg = "";
                    $scope.loadingUserData = "";
                    $("#closeUserEdit").click();
                  }, function myError(response) {
                  console.log("error");
                  console.log(response)
                    $scope.msg = response.data.msg;
                  });
              }
});

</script>

</body>
</html>