# Mapnemite

Pokemon Go companion site.

## Setup

Requires the following environment variables:
```
#ElasticSearch host url including username and password.
SEARCHBOX_URL=
#Google Cloud Messaging key.
GCM_KEY=
```

Then run `./gradlew bootRun`.

## Frontend Development

First run `npm install` to install dependencies.  Then `npm run package` to build the compiled bundle file from JS in the src/main/js directory.