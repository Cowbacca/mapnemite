# Mapnemite

https://www.mapnemite.com

Pokemon Go companion site for showing active lures.  Chrome users receive push notifications when a lure is activated near their last known location.  Android users can add the web app to their homescreen for a better experience.

## To use

Click on a Pokestop to signify that you have activated a lure at that location.

## About

Pokestop locations currently based off this map: https://www.google.com/maps/d/u/0/viewer?hl=en&authuser=0&mid=1lreAi3mhRKdY0SgAxaTJEQicOXg
This is missing Pokestops in several areas.  In future, Pokestops can be gathered from the Pokemon Go API to ensure there are no missing stops.

For suggestions or bug requests, please raise an issue here: https://github.com/cowbacca/mapnemite/issues

This is a free open source project under the MIT License.  Feel free to raise pull requests with any changes.

## Future Features
* Ability to undo activating a lure.
* Use Pokemon Go API to collect pokestop information OR allow users to place lures where no Pokestops are known to exist.
* Notification configuration, e.g. turn notifications on/off easily, set notification radius, set specific times of day when notifications should be sent, etc.

## Development

### Requirements
* node/npm
* Java 8

### Setup

Requires the following environment variables:
```
#ElasticSearch host url including username and password.
SEARCHBOX_URL=
#Google Cloud Messaging key.
GCM_KEY=
```

Then run `./gradlew bootRun`.

### Frontend Development

First run `npm install` to install dependencies.  Then `npm run package` to build the compiled bundle file from JS in the src/main/js directory.