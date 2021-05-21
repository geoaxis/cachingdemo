# cachingdemo

Commands to get k8s on skaffold running
```
mkdir -p k8s && cd k8s;
kubectl create deployment cachingdemo --image=cachingdemo --dry-run=client -o=yaml > 01-deployment.yaml;
kubectl create service clusterip cachingdemo --tcp=8080:8080 --dry-run=client -o=yaml > 02-service.yaml;
skaffold init --XXenableBuildpacksInit
skaffold dev
```


```
apiVersion: skaffold/v2beta16
kind: Config
metadata:
  name: cachingdemo
build:
  artifacts:
  - image: cachingdemo
    buildpacks:
      builder: gcr.io/buildpacks/builder:v1
deploy:
  kustomize:
    paths:
    - k8s
? Do you want to write this configuration to skaffold.yaml? Yes
Configuration skaffold.yaml was writtenon to skaffold.yaml? (y/N) y
You can now run [skaffold build] to build the artifacts
or [skaffold run] to build and deploy
or [skaffold dev] to enter development mode, with auto-redeploy

```
