(function () {

	//this directives is for expending the sectionPage with fullheight to the hole window
	 var as =  angular.module('ssmApp.directives', []);
	 as.directive('fullHeight', function($window){
			return {
				restrict: 'A',
				link: function(scope, element, attrs) {
					var offsetHeight = attrs.offset | 0;
					element.css('min-height', ($window.innerHeight - offsetHeight) + 'px');
					// handle resize, fix footer position. Now only support fixed mode
				  angular.element($window).bind('resize', function(e) {
				    element.css('min-height', (e.target.innerHeight - offsetHeight) + 'px');
				  })
				}
			}
	});
}());

