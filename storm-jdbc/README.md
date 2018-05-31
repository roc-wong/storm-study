# Flux 部署拓扑


### 部署

```bash
storm jar storm-jdbc-1.0.0-SNAPSHOT-shaded.jar org.apache.storm.flux.Flux -r application.yaml --filter application.properties
```

YAML优化版：
```bash
storm jar storm-jdbc-1.0.0-SNAPSHOT-shaded.jar org.apache.storm.flux.Flux -r application-plus.yaml --filter application.properties
```


### dry-run

仅构建、验证和打印这个拓扑的相关信息

```bash
storm jar storm-jdbc-1.0.0-SNAPSHOT-shaded.jar org.apache.storm.flux.Flux -r application.yaml --filter application.properties
```

