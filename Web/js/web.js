  var panoramaLeft;
  var panoramaRight;
  var outsideGoogle;

  function initPano() {
  // Note: constructed panorama objects have visible: true
  // set by default.
  var mapSetings = {
        position: {lat: web.lat, lng: web.lng},
        addressControlOptions: {
          position: google.maps.ControlPosition.BOTTOM_CENTER
        },
		disableDefaultUI: true,

        zoomControlOptions: {
          style: google.maps.ZoomControlStyle.SMALL
        },
        enableCloseButton: false
  };
  panoramaLeft = new google.maps.StreetViewPanorama(
      document.getElementById('map1'), mapSetings);
	panoramaRight = new google.maps.StreetViewPanorama(
      document.getElementById('map2'), mapSetings);

}

var web = {
  lat : 42.345573,
  lng : -71.098326,
  webPov : {
        heading: 34,
        pitch: 10,
        zoom: 1
      },

  initMaps : function(){
    window.initPano();
  },

  setLocation: function (lat,lng){
    web.lat = lat;
    web.lng = lng;
    panoramaLeft.setPosition({lat:web.lat, lng:web.lng});
    panoramaRight.setPosition({lat:web.lat, lng:web.lng});
  },

  setZPos: function(z){
    web.webPov.pitch = z;
    panoramaLeft.setPov(web.webPov);
    panoramaRight.setPov(web.webPov);
  },

  setXPos : function(x){
    web.webPov.heading = x;
    panoramaLeft.setPov(web.webPov);
    panoramaRight.setPov(web.webPov);
  }
}




