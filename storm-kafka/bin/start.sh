#!/bin/bash -
storm jar storm-kafka-1.0.0-SNAPSHOT-shaded.jar org.apache.storm.flux.Flux -r application.yaml --filter application.properties