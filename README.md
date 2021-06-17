# Enclave

Own land and have a good time.

### What is it?

This is a Bukkit/Spigot plugin made for Minecraft servers. It is used to organized players into groups, or Enclaves. It
provides land protection and social features.

### Local Testing

#### Test Server

It is reccomended to create a test server in the root directory of this project. Make sure you do not include it in git.

```
    /Enclave
      [ |- /server  ]
            -| /plugins
            -| /start.sh
        |- /scripts
        |- /src
        |- /targets
```

There should be a start.sh with executable permissions. This fill will be called to start the server. Concurrent reloads
will connect to the server's screen session to reload.

On packaging the maven project, the shaded jar (containing mongodb) will be places in the project root directory. If you
would like it to go directly to a local server's plugin folder, add the mvn argument -DoutputDir.

#### Package Example

`mvn package -DoutputDir=/Users/you/java/Enclave/server/plugins -f pom.xml`

#### Automatically Reload

After successfully building and compiling the jar, you can run a maven command to reload the server.

#### Exec Example

`mvn exec:exec`

This command will run the reload script /scripts/reload.sh