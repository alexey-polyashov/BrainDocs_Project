angular.module('app').controller('filesUploadController', function ($scope, $http, $localStorage) {

    $scope.baseURL_documentsAPI = baseURL_documentsAPI;


    $scope.fileUpload = function(element){

console.log(event);

        let fileData = event.target.files[0];

        let fileIdString = event.target.id;
        let fileId = Number.parseInt(fileIdString.replace("fileData", ""));
        let fileType = fileData.name.split('.').pop();

        let idValue = angular.element(document.querySelector("#fileId"+fileId)).value;

        let fileDescribe = {
            name: $scope.upload.file[fileId].name,
            description: $scope.upload.file[fileId].description,
            originalFilename: fileData.name,
            fileType: fileType,
            author: {id: $scope.upload.authorId}
        };

        let methodName = "upload_file/";

        if(idValue && idValue>0){
            fileDescribe.id = idValue;
            methodName = "change_file/";
        }

        let formData = new FormData();
        formData.set("fileDescribe", JSON.stringify(fileDescribe));
        formData.set("file", fileData);

        //for(let [name, value] of formData) {
        //  console.log(`${name} = ${value}`); // key1=value1, потом key2=value2
        //}

        $http.post(baseURL_documentsAPI + "upload_file/" + $scope.upload.docId, formData,
            {withCredentials: false,
              headers: {
                'Content-Type': undefined
              },
              transformRequest: angular.identity,
              params: {
                formData
              },
              responseType: "arraybuffer"
             }
        )
        .then(function(response){
            var data = response.data;
console.log(response);
            angular.element(document.querySelector("#fileId"+fileId)).value = data;
        })
        .catch(function(error){
            alert("Error " + error.status + "\n" + error.data);
        });

    };

    $scope.saveFiles = function(element){

        for(let fileId=0; i<1; i++){

            let idValue = angular.element(document.querySelector("#fileId"+fileId)).value;

            let fileDescribe = {
                name: $scope.upload.file[fileId].name,
                description: $scope.upload.file[fileId].description,
                author: {id: $scope.upload.authorId}
            };

            let methodName = "change_file/";

            if(idValue && idValue>0){
                fileDescribe.id = idValue;
            }

            let formData = new FormData();
            formData.set("fileDescribe", JSON.stringify(fileDescribe));

            $http.post(baseURL_documentsAPI + "upload_file/" + $scope.upload.docId, formData,
                {withCredentials: false,
                  headers: {
                    'Content-Type': undefined
                  },
                  transformRequest: angular.identity,
                  params: {
                    formData
                  },
                  responseType: "arraybuffer"
                 }
            )
            .catch(function(error){
                alert("Error " + error.status + "\n" + error.data);
            });

        }

    };



});