  var panoramaLeft;
  var panoramaRight;
  var outsideGoogle;
  var mapSetings = {
    center: mPos,
    zoom: 1,
    streetViewControl: true
  };
  var mPos = {lat : 40.757981, lng : -73.985580};
  var webPov = {
        heading: 34,
        pitch: 10,
        zoom: 1
      };

  var markerIcon = 'http://icons.iconarchive.com/icons/yellowicon/game-stars/256/Mario-icon.png';

 

  var map1, map2;
  var marker1,marker2;

  function initPano() {
  
  map1 = new google.maps.Map(document.getElementById('map1'), mapSetings);
  map2 = new google.maps.Map(document.getElementById('map2'), mapSetings);
  marker1 = new google.maps.Marker({
     position: mPos,
     icon: markerIcon,
     title: 'Cafe'
  });
  marker2 = new google.maps.Marker({
     position: mPos,
     icon: markerIcon,
     title: 'Cafe'
  });
  marker1.setMap(map1);
  marker2.setMap(map2);
  panoramaLeft = map1.getStreetView();
  panoramaRight = map2.getStreetView();

  panoramaLeft.setPosition(mPos);
  panoramaRight.setPosition(mPos);
  panoramaLeft.setPov(/** @type {google.maps.StreetViewPov} */(webPov));
  panoramaRight.setPov(/** @type {google.maps.StreetViewPov} */(webPov));
  panoramaLeft.setVisible(true);
  panoramaRight.setVisible(true);
}

var web = {
  lat : mPos.lat,//42.345573,
  lng : mPos.lng,//-71.098326,
  setLocation: function (lat,lng){
    web.lat = lat;
    web.lng = lng;
    panoramaLeft.setPosition({lat:web.lat, lng:web.lng});
    panoramaRight.setPosition({lat:web.lat, lng:web.lng});
  },

  setZPos: function(z){
    webPov.pitch = z;
    panoramaLeft.setPov(webPov);
    panoramaRight.setPov(webPov);
  },

  setXPos : function(x){
    webPov.heading = x;
    panoramaLeft.setPov(webPov);
    panoramaRight.setPov(webPov);
  }
}