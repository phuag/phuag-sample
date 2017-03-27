//Define a function scope, variables used inside it will NOT be globally visible.
(function() {

	var
	//the HTTP headers to be used by all requests
		httpHeaders,
		//the message to be shown to the user
		message,
		//Define the main module.
		//The module is accessible everywhere using "angular.module('angularspring')", therefore global variables can be avoided totally.
		as = angular.module('ssmApp', ['ui.router', 'ngResource', 'ngCookies', 'ui.bootstrap', 'ngMessages', 'ssmApp.i18n', 'ssmApp.services', 'ssmApp.controllers','ssmApp.directives', 'ssmApp.filters']);

	as.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
		function($stateProvider, $urlRouterProvider, $httpProvider) {
			//configure the rounting of ng-view
			$urlRouterProvider
				.when('/', '/login')
				.when('/main','/main/staffs');
			
			$stateProvider
				.state('root',{
					abstract: true,					
					views: {
						'header': {
							templateUrl: 'partials/header.html'
						},
						'sidebar': {
							templateUrl: 'partials/sidebar.html'
						},
						'main': {
							templateUrl: 'partials/frame.html'
						},
						'footer': {
							templateUrl: 'partials/footer.html'
						}
					}
				})
				.state('root.login', {
					url: '/login',
					views:{
						'master@root': {
							templateUrl: 'partials/login.html',
							controller: 'LoginController'
						}                        
					}                    
				})
				.state('root.register', {
					url: '/register',
					views:{
						'master@root': {
							templateUrl: 'partials/register.html',
							controller: 'RegisterController'
						}
					}                    
				})
				.state('root.lock', {
					url: '/lock',
					views:{
						'master@root': {
							templateUrl: 'partials/lock.html',
							controller: 'LockController'
						}
					}                    
				})
				.state('root.main',{
					abstract: true,
				})
				.state('root.main.staffs',{
					url: "/main/staffs",
					views:{
						'master@root': {
							templateUrl: 'partials/posts/staffs.html',
							controller: 'StaffsController'
						}
					}
					
				})
				.state('root.main.anotherLink',{
					url: "/main/anotherLink",
					views:{
						'master@root': {
							templateUrl: 'partials/posts/anotherLink.html',
							replace: true
						}
					}
					
				})
				.state('root.main.404', {
					url: "/main/404",
					views:{
						'master@root': {
							templateUrl: 'partials/404.html',
							replace: true
						}
					}
					
				});



			// $stateProvider
			//     .state('staffs', {
			//         url: "/staffs",
			//         views: {
			//             "menuView": {
			//                 templateUrl: 'partials/mainMenu.html'
			//             },
			//             "contentView": {
			//                 templateUrl: 'partials/staffs.html',
			//                 controller: 'StaffsController'
			//             }
			//         }
					
			//     })
			//     .state('anotherLink', {
			//         url: "/anotherLink",
			//         views: {
			//             "menuView": {
			//                 templateUrl: 'partials/mainMenu.html'
			//             },
			//             "contentView": {
			//                 templateUrl: 'partials/anotherLink.html'
			//             }
			//         }
					
			//     })
			//     .state('404', {
			//         url: "/404",
			//         views: {
			//             "menuView": {
			//                 templateUrl: 'partials/mainMenu.html'
			//             },
			//             "contentView": {
			//                templateUrl: 'partials/404.html'
			//             }
			//         }
			//     });


			//configure $http to catch message responses and show them
			$httpProvider.interceptors.push(function($q) {
				var setMessage = function(response) {
					//if the response has a text and a type property, it is a message to be shown
					if (response.data.text && response.data.type) {
						message = {
							text: response.data.text,
							type: response.data.type,
							show: true
						};
					}
				};

				return {
					//this is called after each successful server request
					'response': function(response) {
						// console.log('request:' + response);
						setMessage(response);
						return response || $q.when(response);
					},
					//this is called after each unsuccessful server request
					'responseError': function(response) {
						//console.log('requestError:' + response);
						setMessage(response);
						return $q.reject(response);
					}

				};
			});

			$httpProvider.interceptors.push(function($rootScope, $q) {

				return {
					'request': function(config) {
						// console.log('request:' + config);
						return config || $q.when(config);
					},
					'requestError': function(rejection) {
						// console.log('requestError:' + rejection);
						return rejection;
					},
					//success -> don't intercept
					'response': function(response) {
						// console.log('response:' + response);
						return response || $q.when(response);
					},
					//error -> if 401 save the request and broadcast an event
					'responseError': function(response) {
						console.log('responseError:' + response);
						if (response.status === 401) {
							var deferred = $q.defer(),
								req = {
									config: response.config,
									deferred: deferred
								};
							$rootScope.requests401.push(req);
							$rootScope.$broadcast('event:loginRequired');
							return deferred.promise;
						}
						return $q.reject(response);
					}

				};
			});


			httpHeaders = $httpProvider.defaults.headers;
		}
	]);


	as.run(function($rootScope, $http, $location, base64) {
		//make current message accessible to root scope and therefore all scopes
		$rootScope.message = function() {
			return message;
		};

		/**
		 * Holds all the requests which failed due to 401 response.
		 */
		$rootScope.requests401 = [];

		$rootScope.$on('event:loginRequired', function() {
			//$('#login').modal('show');
			$location.path('/login');
		});

		/**
		 * On 'event:loginConfirmed', resend all the 401 requests.
		 */
		$rootScope.$on('event:loginConfirmed', function() {
			var i,
				requests = $rootScope.requests401,
				retry = function(req) {
					$http(req.config).then(function(response) {
						req.deferred.resolve(response);
					});
				};

			for (i = 0; i < requests.length; i += 1) {
				retry(requests[i]);
			}

			$rootScope.requests401 = [];
			$location.path('/main');
		});

		/**
		 * On 'event:loginRequest' send credentials to the server.
		 */
		$rootScope.$on('event:loginRequest', function(event, username, password) {
			httpHeaders.common['Authorization'] = 'Basic ' + base64.encode(username + ':' + password);
			console.log('httpHeaders.common[\'Authorization\']@' + httpHeaders.common['Authorization'] + ':::' + username + ':' + password);
			$http.post('api/me')
				.success(function(data) {
					$rootScope.authenticated = true;
					$rootScope.name = data.username;
					$rootScope.$broadcast('event:loginConfirmed');
				})
				.error(function(data) {
					console.log('login failed...@' + data);
				});
		});

		/**
		 * On 'logoutRequest' invoke logout on the server and broadcast 'event:loginRequired'.
		 */
		$rootScope.$on('event:logoutRequest', function() {
			$rootScope.authenticated = false;
			delete $rootScope.name;
			delete httpHeaders.common['Authorization'];
			$http.post('api/logout')
				.success(function(data){
					console.log('logout success...@' + data);
				})
				.error(function(data){
					console.log('logout error...@' + data);
				});
		});

		var routesOpenToPublic = [];
		/*
		angular.forEach($routeProvider.routes, function(route, path) {
			// push route onto routesOpenToPublic if it has a truthy publicAccess value
			route.publicAccess && (routesOpenToPublic.push(path));
		});*/

		$rootScope.$on('$stateChangeStart', function(event, nextLoc, currentLoc) {
			console.log('fire event@$stateChangeStart');
			var closedToPublic = (-1 === routesOpenToPublic.indexOf($location.path()));
			if (closedToPublic && !$rootScope.authenticated) {
				//console.log('login required...');
				$rootScope.$broadcast('event:loginRequired');
			} else if (!!$rootScope.authenticated) {
				//console.log('already logged in...');
				if (!!nextLoc && nextLoc.url == '/login') {
					$location.path('/main');
				} else {
					//do nothing...
				}
			}
		});

		//$rootScope.$on('$viewContentChange', funtion());
		//check the networking connection.

		$http.get('api/ping')
			.success(function(data) {
				console.log("ping result@" + data);
			})
			.error(function(data) {
				$rootScope.message = {
					text: 'Network connection eror!',
					type: 'danger',
					show: true
				};
			});
	});
}());
