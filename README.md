# Docker Boxes Demo

This setup is inspired and uses Code from [the jobdsl gradle example](https://github.com/sheehan/job-dsl-gradle-example) by [sheehan](https://github.com/sheehan).

It is included and published to give an overall overview on how everything integrates and can be used with our [docker-boxes](https://github.com/i-net-software/docker-boxes) project that is used to automatically set up a Jenkins with jobs, auto discovery using Consul and binding all servers behind an Nginx proxy.

## Getting started

Check out this repository and run:

    gradle jenkins:start

This will deploy the jenkins container locally. Add ```-DDOCKER_HOST=x.x.x.x:yy``` for a concrete docker running machine.
