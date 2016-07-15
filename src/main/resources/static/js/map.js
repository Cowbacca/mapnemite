'use strict';

var map;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {
            lat: 50.8232076,
            lng: -0.1545839
        },
        zoom: 15
    });

    fetch('/lures?latitude=' + 50.8232076 + '&longitude=' + -0.1545839 + '&radius=20').then(function (resp) {
        return resp.json();
    }).then(function (json) {
        return json.lures;
    }).then(function (lures) {
        return lures.forEach(marker);
    });

    google.maps.event.addListener(map, 'click', function (event) {
        var location = event.latLng;
        fetch('/lures?latitude=' + location.lat() + '&longitude=' + location.lng(), {
            method: 'POST'
        }).then(function (resp) {
            return resp.json();
        }).then(marker);
    });

    if ('serviceWorker' in navigator) {
      console.log('Service Worker is supported');

      navigator.serviceWorker.addEventListener('message', function(event) {
          marker(event.data);
        });

      navigator.serviceWorker.register('sw.js').then(function() {
        return navigator.serviceWorker.ready;
      }).then(function(reg) {
        console.log('Service Worker is ready :^)', reg);
        reg.pushManager.subscribe({userVisibleOnly: true}).then(function(sub) {
          fetch(`/subscribers`, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sub),
          })
          console.log('endpoint:', sub.endpoint);
        });
      }).catch(function(error) {
        console.log('Service Worker error :^(', error);
      });
    }

    function marker(lure) {
        var location = new google.maps.LatLng(lure.latitude, lure.longitude);
        placeMarker(location, lure.expiresAt);
    }

    function placeMarker(location, expiresAt) {
        new google.maps.Marker({
            position: location,
            map: map
        });

        new MapLabel({
            text: 'Expires at: ' + moment(expiresAt).format('HH:mm'),
            position: location,
            map: map,
            fontSize: 20
        });
    }
}