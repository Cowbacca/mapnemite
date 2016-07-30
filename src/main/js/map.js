import moment from 'moment';
import throttle from 'lodash/throttle';
import google from 'google';
import { parse } from 'query-string';

export function initMap() {
    const { lat, long } = parse(window.location.search);
    const center = {
        lat: lat ? parseFloat(lat) : 50.8225,
        lng: long ? parseFloat(long) : 0.1372,
    };

    const map = new google.maps.Map(document.getElementById('map'), {
        center,
        zoom: 16,
        minZoom: 12,
        maxZoom: 18,
    });

    map.data.loadGeoJson('geo_stops.json');
    map.data.setStyle({
        icon: {
            url: 'http://orig08.deviantart.net/ea64/f/2011/175/b/2/pokeball_free_icon_by_scratch_the_hedgie-d3jwgjs.gif',
            anchor: new google.maps.Point(25, 25),
        }
    });
    map.data.addListener('click', addLure);

    google.maps.event.addListener(map, 'bounds_changed', throttle(findMarkers, 1000));

    if (!lat && !long && "geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(onPositionFound);
    } else {
        console.log("No GeoLocation. :(");
    }

    function onPositionFound(position) {
        map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude))
        if ('serviceWorker' in navigator) {
            console.log('Service Worker is supported');

            if (Notification.permission === "granted") {
                registerServiceWorker(position);
            } else {
                Notification.requestPermission(permission => {
                    if (permission === "granted") {
                        registerServiceWorker(position);
                    } else {
                        alert("You will not receive notifications about new nearby lures.")
                    }
                });
            }
        }
    }

    function findMarkers(event) {
        var bounds = map.getBounds();

        var ne = bounds.getNorthEast();
        var sw = bounds.getSouthWest();

        fetch('/lures?neLat=' + ne.lat() + '&neLong=' + ne.lng() + '&swLat=' + sw.lat() + '&swLong=' + sw.lng())
            .then(function(resp) {
                return resp.json();
            }).then(function(json) {
                return json.lures;
            }).then(function(lures) {
                return lures.forEach(marker);
            });
    }

    function marker(lure) {
        var location = new google.maps.LatLng(lure.latitude, lure.longitude);
        placeMarker(location, lure.expiresAt);
    }

    function addLure(event) {
        var location = event.latLng;
        fetch('/lures?latitude=' + location.lat() + '&longitude=' + location.lng(), {
            method: 'POST'
        }).then(function(resp) {
            return resp.json();
        }).then(marker);
    }

    function placeMarker(location, expiresAt) {
        new google.maps.Marker({
            position: location,
            map: map,
        });

        new MapLabel({
            text: moment(expiresAt).format('HH:mm'),
            position: location,
            map: map,
            fontSize: 20,
        });
    }

    function registerServiceWorker(position) {
        navigator.serviceWorker.addEventListener('message', event => marker(event.data));

        navigator.serviceWorker.register('sw.js')
            .then(() => navigator.serviceWorker.ready)
            .then(function(reg) {
                console.log('Service Worker is ready :^)', reg);
                reg.pushManager.subscribe({
                        userVisibleOnly: true
                    })
                    .then(sub => {
                        return {
                            ...sub.toJSON(),
                            latitude: position.coords.latitude,
                            longitude: position.coords.longitude,
                        };
                    })
                    .then(pushSubscription => {
                        fetch('/subscribers', {
                            method: 'PUT',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(pushSubscription),
                        });
                    });
            }).catch(function(error) {
                console.log('Service Worker error :^(', error);
            });
    }
}
