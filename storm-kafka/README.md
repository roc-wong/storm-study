# Flux 部署拓扑

> 仅以storm-kafka为例说明。

### 部署

```bash
storm jar storm-kafka-1.0.0-SNAPSHOT-shaded.jar org.apache.storm.flux.Flux -r application.yaml --filter application.properties
```

### dry-run

仅构建、验证和打印这个拓扑的相关信息

```bash
storm jar storm-kafka-1.0.0-SNAPSHOT-shaded.jar org.apache.storm.flux.Flux -r application.yaml --filter application.properties
```