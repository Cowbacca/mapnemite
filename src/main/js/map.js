var map;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {
            lat: 50.8232076,
            lng: -0.1545839
        },
        zoom: 15
    });

    fetch(`/lures?latitude=${50.8232076}&longitude=${-0.1545839}&radius=20`)
        .then(resp => resp.json())
        .then(json => json.lures)
        .then(lures => lures.forEach(marker));

    google.maps.event.addListener(map, 'click', function(event) {
        const location = event.latLng;
        fetch(`/lures?latitude=${location.lat()}&longitude=${location.lng()}`, {
                method: 'POST'
            }).then(resp => resp.json())
            .then(marker);
    });

    function marker(lure) {
        const location = new google.maps.LatLng(lure.latitude, lure.longitude);
        placeMarker(location, lure.expiresAt);
    }

    function placeMarker(location, expiresAt) {
        new google.maps.Marker({
            position: location,
            map: map,
        });

        new MapLabel({
            text: `Expires at: ${moment(expiresAt).format('HH:mm')}`,
            position: location,
            map: map,
            fontSize: 20,
        });
    }
}
