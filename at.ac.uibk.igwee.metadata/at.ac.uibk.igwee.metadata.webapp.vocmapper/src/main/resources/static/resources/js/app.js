/**
 * Created by Joseph on 13.04.2015.
 */

var myApp = angular.module('myApp', ['ui.router', 'angularFileUpload']);


/* Global Services */

myApp.service('queryQueueService', ['$rootScope','$http', function($rootScope, $http) {

    var queryQueueContainer = this;

    this.getQueryQueue = function() {
        if (typeof this.queryQueue === 'undefined') {
            this.loadQueryQueue();
            return {};
        }
        return this.queryQueue;
    };

    this.loadQueryQueue = function() {

        queryQueueContainer.counter++;

        $http.get('app/edit/')
            .success(function(data) {
                queryQueueContainer.queryQueue = data;
                console.log(queryQueueContainer.queryQueue);
                $rootScope.$broadcast("renewQueryQueue");
            });

    };

    console.log("queryQueueService created.");

}]);

myApp.service('singleResultService', ['queryQueueService', '$http', '$rootScope', function(queryQueueService, $http, $rootScope){

    var con = this;

    con.position = 0;

    this.setPosition = function(pos) {
        con.position = pos;
        con.url = "";
        $rootScope.$broadcast('editResult');
    };

    this.getPosition = function() {
        return con.position;
    };

    this.getSingleResult = function(container) {

        var url = "app/edit/" + con.position;
        $http.get(url)
            .success(function(data) {
                container.result = data;
            });

    };

    con.url = "";

    this.setUrl = function(url) {
        con.url = url;
        $rootScope.$broadcast('viewUrl');
    };

    this.getUrl = function() {
        return con.url;
    };

    this.fixUrlAndClear = function() {
        console.log("Clear and fix position " + con.position + " to url " + con.url);
        var postUrl = "app/edit/" + con.getPosition() + "/fixToAndClear";
        var field = {url:con.getUrl()};
        $http({
            url: postUrl,
            method: 'POST',
            data: $.param(field),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
        }).success(function() {
            $rootScope.$broadcast('editResult');
            queryQueueService.loadQueryQueue();
        });
        this.setUrl("");
    };

    this.fixUrl = function() {
        console.log("Fix position " + con.position + " to url " + con.url);
        var postUrl = "app/edit/" + con.getPosition() + "/fixTo";
        var field = {url:con.getUrl()};
        $http({
            url: postUrl,
            method: 'POST',
            data: $.param(field),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
        }).success(function() {
            $rootScope.$broadcast('editResult');
            queryQueueService.loadQueryQueue();
        });
        this.setUrl("");
    };

    this.removeUrl = function() {
        console.log("Remove from position " + con.getPosition() + " url " + con.getUrl());

        var postUrl = "app/edit/" + con.getPosition() + "/remove";
        var field = {url: con.getUrl()};
        $http({
            url: postUrl,
            method: 'POST',
            data: $.param(field),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
        }).success(function() {
            $rootScope.$broadcast('editResult');
        });
        this.setUrl("");
    };

}]);


/* Directives */


myApp.directive('queryQueueShort', function() {
    return {
        restrict: 'E',
        templateUrl: 'templates/queryqueueShort.html'
    };
});


myApp.directive('editPendingQueue', function() {
    return {
        restrict: 'E',
        templateUrl: 'templates/editPendingQueue.html'
    };
});

/* Controllers */



myApp.controller("QueryQueueController", ['queryQueueService','$scope',
    function(queryQueueService, $scope) {

        var showing = 'pending';

        this.setShowing = function(panel) {
            showing = panel;
        };

        this.isShowing = function(panel) {
            return showing === panel;
        };

        $scope.queryQueue = queryQueueService.getQueryQueue();

        if (typeof $scope.queryQueue === 'undefined') {
            queryQueueService.loadQueryQueue();
        }

        $scope.$on("renewQueryQueue", function() {
            $scope.queryQueue = queryQueueService.getQueryQueue();
        });
}]);

myApp.controller('FileUploadController', ['queryQueueService', '$scope', '$upload', '$http',
    function(queryQueueService, $scope, $upload, $http) {

    $scope.progressFile = {};

    $scope.excelCsvFile = {};

    $scope.excelCsvEncoding = "";


    $scope.uploadProgress = function() {
        if ($scope.progressFile) {
            $upload.upload(
                {
                    url: 'app/upload/xml',
                    file: $scope.progressFile,
                    fileFormDataName: 'file'
                }
            ).success(function(data) {
                    console.log("Upload success: " + data);
                    queryQueueService.loadQueryQueue();
                });
        }
    };

    $scope.uploadExcelCsv = function() {
        console.log("uploadExcelCsv called");
        if ($scope.excelCsvFile) {
            $upload.upload(
                {
                    url: 'app/upload/excelcsv',
                    file: $scope.excelCsvFile,
                    fileFormDataName: 'file',
                    fields: {
                        'encoding': $scope.excelCsvEncoding
                    }
                }
            ).success(function(data) {
                    console.log("Upload success: " + data);
                    queryQueueService.loadQueryQueue();
                });
        }
    };

    $scope.clearProgress = function() {
        console.log("Clear progress called.");
        $http({
            url: 'app/upload/clear',
            method: 'POST'
        }).success(function() {
            alert("The progress has been deleted.");
            queryQueueService.loadQueryQueue();
            });
    };

}] );


myApp.controller("PreQueryController", ['queryQueueService', '$scope', '$http','$timeout',
    function(queryQueueService, $scope, $http, $timeout) {

        $scope.workerStatus = {};
        $scope.workerStatus.finished = true;
        $scope.workerStatus.previewAvailable = false;

        $scope.queryQueue = queryQueueService.getQueryQueue();

        var autoUpdateWorker = false;

        $scope.updateWorkerStatus = function() {
            $scope.workerStatus.finished = false;
            $http.get("app/prequery/finished").success(function(data) {
                var fin = data;
                if (!$scope.workerStatus.finished && fin) {
                    queryQueueService.loadQueryQueue();
                    $scope.workerStatus.finished = fin;
                    autoUpdateWorker = false;
                    $scope.preview();
                }
                if (autoUpdateWorker) {
                    $timeout($scope.updateWorkerStatus, 1000, false);
                }
            });
            $http.get("app/prequery/previewAvailable").success(function(data) {
                var pa = data;
                $scope.workerStatus.previewAvailable = pa;
            });
        };

        $scope.$on('$destroy', function() {
            autoUpdateWorker = false;
        });

        $scope.$on("renewQueryQueue", function() {
            $scope.queryQueue = queryQueueService.getQueryQueue();
        });
        $scope.updateWorkerStatus();

        $scope.previewData = {};

        $scope.preview = function() {
            $http.get('app/prequery/preview').success(function(data) {
                $scope.previewData = data;
            });
        };

        $scope.accept = function() {
            $http.post('app/prequery/accept').success(function() {
                console.log("Accept called");
                $scope.previewData = {};
                $scope.workerStatus.previewAvailable = false;
                queryQueueService.loadQueryQueue();
            });
        };

        $scope.reject = function() {
            $http.post("app/prequery/reject").success(function() {
                console.log("Reject called");
                $scope.previewData = {};
                $scope.workerStatus.previewAvailable = false;
                queryQueueService.loadQueryQueue();
            });
        };

        $scope.startQuery = function() {
            doQuery(false);
        };
        $scope.startRequery = function() {
            doQuery(true);
        };

        var doQuery = function(requeryValue) {
            var field = {'requery': requeryValue};
            $http({
                url: 'app/prequery/start',
                data: $.param(field),
                method: 'POST',
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
            }).success(function() {
                console.log("requery called.");
                autoUpdateWorker = true;
                $scope.updateWorkerStatus();
            });
        };

}]);

myApp.controller('AddQueryController', ['queryQueueService','$http', '$scope',
    function(queryQueueService, $http, $scope) {

    $scope.addForm = {};

    $scope.addForm.authorities = {};

    var authsBegin = [{
        name: 'Geonames',
        url: 'http://geonames.org',
        selected: false
    }, {
        name: 'GND',
        url: 'http://d-nb.info',
        selected: false
    }, {
        name: 'VIAF',
        url: 'http://www.viaf.org',
        selected: false
    }, {
        name: 'Wikidata',
        url: 'http://wikidata.org',
        selected: false
    }];

    $scope.auths = JSON.parse(JSON.stringify(authsBegin));

    $scope.addQuery = function() {
        console.log($scope.addForm);
        console.log($scope.auths);
        var fields = {
            name: $scope.addForm.name,
            queryString: $scope.addForm.queryString,
            info: $scope.addForm.info,
            type: $scope.addForm.type
        };
        var paramString = $.param(fields);
        $scope.auths.forEach(function(authNow) {
            if (authNow.selected)
                paramString = paramString + "&authorities=" + authNow.name;

        });
        console.log(paramString);
        $http({
            url: 'app/prequery/add',
            data: paramString,
            method: 'POST',
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
        }).success(function() {
            $scope.addForm = {};
            $scope.auths = JSON.parse(JSON.stringify(authsBegin));
            queryQueueService.loadQueryQueue();
        });
    };

}]);


myApp.controller('EditPendingListController', ['queryQueueService', '$http', '$scope', function(queryQueueService, $http, $scope) {

    $scope.queryQueue = queryQueueService.getQueryQueue();



    if (typeof $scope.queryQueue === 'undefined') {
        queryQueueService.loadQueryQueue();
    }

    $scope.$on('renewQueryQueue', function(){
        $scope.queryQueue = queryQueueService.getQueryQueue();
    });

    $scope.removeQuery = function(position) {
        console.log("Remove pending query at position " + position);
        var url = "app/prequery/remove/" + position;
        $http({
            url: url,
            method: 'POST'
        }).success(function() {
            queryQueueService.loadQueryQueue();
        });
    };

}]);

myApp.controller('EditPendingQueueController', ['$scope', function($scope) {
    this.showing = 'list';
    this.isShowing = function(panel) {
        return this.showing === panel;
    };
    this.setShowing = function(panel) {
        this.showing = panel;
    };

    $scope.setupAuthorities = {};

    $scope.loadSetupAuthorities = function() {
        $http.get("app/prequery/authorities")
            .success(function(data) {
                console.log(data);
                $scope.setupAuthorities = data;
            });
    };

    $scope.setUpAuthorities = function() {
        console.log("Setup authorities called.");

    };

}]);

myApp.controller('QueryQueueListController', ['queryQueueService', 'singleResultService', '$scope', '$http',
    function(queryQueueService, singleResultService, $scope, $http) {

    $scope.queryQueue = {};

    $scope.queryQueue = queryQueueService.getQueryQueue();

    if (!$scope.queryQueue.name)
        queryQueueService.loadQueryQueue();

    $scope.reloadQueryQueue = function() {
        queryQueueService.loadQueryQueue();
    };

    $scope.$on('renewQueryQueue', function() {
        $scope.queryQueue = queryQueueService.getQueryQueue();
    });

    $scope.edit = function(position) {
        singleResultService.setPosition(position);
        console.log(position);
    };

    $scope.removeResult = function(position) {
        console.log("Remove result at " + position);
        var postUrl = "app/edit/removeResult/" + position;
        $http({
            method: 'POST',
            url: postUrl
        }).success(function() {
            queryQueueService.loadQueryQueue();
        });
    };

}]);


myApp.controller('QueryQueueEditController', ['singleResultService', '$scope',
    function(singleResultService, $scope) {

    var con = this;

    con.position = 0;

    con.result = {};

    $scope.$on('editResult', function() {
        con.position = singleResultService.getPosition();
        singleResultService.getSingleResult(con);
        console.log(con.result);
    });

    $scope.viewUrl = function(url) {
        singleResultService.setUrl(url);
    };

}]);

myApp.controller('QueryQueueVocController', ['singleResultService', '$scope',
    function(singleResultService, $scope) {
    $scope.currentUrl = "";

    $scope.$on('viewUrl', function(){
        var currentUrl = singleResultService.getUrl();
        $scope.currentUrl = currentUrl;
    });

    $scope.fixCurrentUrl = function() {
        singleResultService.fixUrl();
    };

    $scope.fixCurrentUrlAndClear = function() {
        singleResultService.fixUrlAndClear();
    };

    $scope.removeCurrentUrl = function() {
        singleResultService.removeUrl();
    };

}]);

myApp.controller('ExportController', ['$scope', function($scope) {

    $scope.hasExportUrl = false;

    $scope.exportForm = {
        delimiter: ',',
        encoding: 'iso_8859-1'
    };

    $scope.exportUrl = {};

    $scope.generateExportUrl = function() {
        var url = "app/export/csv?delimiter=" + $scope.exportForm.delimiter + "&encoding=" + $scope.exportForm.encoding;
        $scope.exportUrl = url;
        $scope.hasExportUrl = true;
    };

    $scope.hideUrl = function() {
        $scope.hasExportUrl = false;
    };

}]);



/* Route configuration */

myApp.config(function($sceDelegateProvider) {

    $sceDelegateProvider.resourceUrlWhitelist([

        'self',

        'http://geonames.org/**',

        'http://www.viaf.org/**',

        'http://www.wikidata.org/**',

        'http://d-nb.info/**'

    ]);

});

myApp.config(function($stateProvider, $urlRouterProvider) {
    //
    // For any unmatched url, redirect to /state1
    $urlRouterProvider.otherwise("/home");
    //
    // Now set up the states
    $stateProvider
        .state('home', {
            url: "/home",
            templateUrl: "templates/home.html"
        })
        .state('upload', {
            url: "/upload",
            templateUrl: "templates/upload.html"
        })
        .state('impressum', {
            url: "/impressum",
            templateUrl: "templates/impressum.html"
        })
        .state('prequery', {
            url: "/prequery",
            templateUrl: "templates/prequery.html"
        })
        .state('edit', {
            url: "/edit",
            templateUrl: "templates/edit.html"
        })
        .state('exportcsv', {
            url: "/export",
            templateUrl: "templates/export.html"
        })
        .state('help', {
            url: "help",
            templateUrl: "templates/help.html"
        })
        ;
});
