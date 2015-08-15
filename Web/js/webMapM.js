var size = {
	that : this,
	width : 0,
	height : 0,
	update : function () {
		that.width = $( document ).width()/2;
		that.height = $( document ).height()/2;
		$( "#map1").width(that.width);
		$( "#map2").height(that.height);
		$( "#map1").width(that.width);
		$( "#map2").height(that.height);
	}
}