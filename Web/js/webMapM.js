var size = {
	width : 0,
	height : 0,
	update : function () {
		this.width = $( document ).width()/2;
		this.height = $( document ).height()/2;
		$( "map1").height() = this.width;
		$( "map2").height() = this.height;
		$( "map1").width() = this.width;
		$( "map2").width() = this.height;
	}
}

