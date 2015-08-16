var size = {
	width : 0,
	height : 0,
	update : function () {
		size.width = $( document ).width()/2;
		size.height = $( document ).height()/2;
		$( "#map1").width(size.width);
		$( "#map2").height(size.height);
		$( "#map1").width(size.width);
		$( "#map2").height(size.height);
	}
}