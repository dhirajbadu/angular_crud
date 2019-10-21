<!DOCTYPE html>
<html>

<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-route.js"></script>

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

app.controller('userCtrl', function($scope, $http) {
  $http({
    method : "GET",
      url : "/api/user/list"
  }).then(function mySuccess(response) {
    console.log(response.data);
    $scope.userList = response.data;
  }, function myError(response) {
    console.log(response.statusText);
  });

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
          $scope.userList = response.data.details;
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
              $scope.userList = response.data.details;
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



app.controller('userAddCtrl', function($scope , $http) {
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
    $scope.msg = "";
  }
});

</script>

</body>
</html>