function initPano() {
  // Note: constructed panorama objects have visible: true
  // set by default.
  var mapSetings = {
        position: {lat: 42.345573, lng: -71.098326},
        addressControlOptions: {
          position: google.maps.ControlPosition.BOTTOM_CENTER
        },
		disableDefaultUI: true,

        zoomControlOptions: {
          style: google.maps.ZoomControlStyle.SMALL
        },
        enableCloseButton: false
  };
  var panorama = new google.maps.StreetViewPanorama(
      document.getElementById('map1'), mapSetings);
	  panorama = new google.maps.StreetViewPanorama(
      document.getElementById('map2'), mapSetings);

}

initPano();
