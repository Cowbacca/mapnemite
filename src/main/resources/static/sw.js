'use strict';

console.log('Started', self);

self.addEventListener('install', function(event) {
  self.skipWaiting();
  console.log('Installed', event);
});

self.addEventListener('activate', function(event) {
  console.log('Activated', event);
});

self.addEventListener('push', function(event) {
  console.log('Push message', event);

  var title = 'New lure added!';

  var json = event.data.json();
  var data = {
    latitude: json.latitude,
    longitude: json.longitude,
    expiresAt: parseInt(json.expiresAt),
    }

  self.clients.matchAll().then(function(clients) {
    clients.forEach(function(client) {
      console.log(client);
      client.postMessage(data);
    });
  });

  event.waitUntil(
    self.registration.showNotification(title, {
      'body': 'Expires at ' + new Date(data.expiresAt).toLocaleTimeString(),
      'icon': 'images/icon.png'
    }));
});

self.addEventListener('notificationclick', function(event) {
    console.log('Notification click: tag ', event.notification.tag);
    event.notification.close();
    var url = 'https://www.mapnemite.com';
    event.waitUntil(
        clients.matchAll({
            type: 'window'
        })
        .then(function(windowClients) {
            for (var i = 0; i < windowClients.length; i++) {
                var client = windowClients[i];
                if (client.url === url && 'focus' in client) {
                    return client.focus();
                }
            }
            if (clients.openWindow) {
                return clients.openWindow(url);
            }
        })
    );
});