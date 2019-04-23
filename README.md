# Kids Game Engine

Building a 3D/2D game engine on top of jMonkeyEngine (http://jmonkeyengine.org/) with a simplified interface for my kids

## Status

[![Build Status](https://travis-ci.org/clubycoder/kids-game-engine.svg?branch=master)](https://travis-ci.org/clubycoder/kids-game-engine)

## Implementation

## Maven

he build is deployed to bintray.

```xml
  <repositories>
    <repository>
      <snapshots>
        <enabled>false</enabled>
      </snapshots>
      <id>bintray-clubycoder-kids</id>
      <name>bintray-clubycoder-kids</name>
      <url>https://dl.bintray.com/clubycoder/kids</url>
    </repository>
  </repositories>
```
```xml
  <dependencies>
    <dependency>
      <groupId>luby.kids</groupId>
      <artifactId>kids-game-engine</artifactId>
      <version>0.1.0</version>
    </dependency>
  </dependencies>