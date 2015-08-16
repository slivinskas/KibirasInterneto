  var panoramaLeft;
  var panoramaRight;
  var outsideGoogle;
  var mapSetings = {
    center: mPos,
    zoom: 1,
    streetViewControl: false,
    disableDefaultUI: true,
    addressControl:false
  };
  var mPos = {lat : 40.757981, lng : -73.985580};

  var webPov = {
        heading: 34,
        pitch: 10,
        zoom: 1
      };

  //var markerIcon = 'http://unconference.adform.com/img/unconference/adform-logo.png';
  var markerIcon = 'https://raw.githubusercontent.com/svytenis/KibirasInterneto/master/Web/icons/';
  
  var map1, map2;
  var markers = new Array(5);
  var markers2 = new Array(5);
  var xx  = 0.000600;
  var xx2 = 0.000600;

  function makeMaker(mainUrl,nr,type,posLat,posLng,map){
    return new google.maps.Marker({
            position: {lat : posLat, lng : posLng},
            icon: mainUrl+nr+"."+type,
            title: 'Cafe',
            map: map
          });
  }

  function initPano() {
    
  map1 = new google.maps.Map(document.getElementById('map1'), mapSetings);
  map2 = new google.maps.Map(document.getElementById('map2'), mapSetings);
  for (var i = markers.length - 1; i >= 0; i--) {
    markers[i]  = makeMaker(markerIcon+"ad",i,"png",mPos.lat+xx,mPos.lng+xx2,map1);
    markers2[i] = makeMaker(markerIcon+"ad",i,"png",mPos.lat+xx,mPos.lng+xx2,map2);
  };

  panoramaLeft  = map1.getStreetView();
  panoramaRight = map2.getStreetView();

  panoramaLeft.setPosition(mPos);
  panoramaRight.setPosition(mPos);
  panoramaLeft.setPov((webPov));
  panoramaRight.setPov((webPov));
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
  },
  changeAds :function(x){
    /*var rootName;
    if(x == true){
      rootName = "ad";
    }else{
      rootName = "photo";
    }*/
    for (var i = markers.length - 1; i >= 0; i--) {
      markers[i].setIcon(markerIcon+x+i+'.png');
      markers2[i].setIcon(markerIcon+x+i+'.png');
    }
  }
}
