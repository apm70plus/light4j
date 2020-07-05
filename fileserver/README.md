# 文件服务组件
文件服务组件支持 二进制文件上传，Base64格式文件上传，富文本上传。支持对图片的标准化处理，原图基础上生成标准图片和缩略图;支持对音、视频文件的标准化处理，音频文件统一转mp3格式，并解析音频时长;视频文件抽取视频第一帧作为封面，并解析视频时长。

## 环境依赖
如果需要音频和视频文件的标准化处理功能，需要服务器安装ffmpeg软件

## 参数配置项

```
framework.fileserver.localstorage.path # 本地文件存储根目录
framework.fileserver.normalization.tool.ffmpeg # 标准化工具ffmpeg目录，默认值ffmpeg，建议配置环境变量ffmpeg
framework.fileserver.normalization.tool.silkconverter # 标准化微信音频转mp3工具配置，默认值silkconverter，建议配置环境变量silkconverter
framework.fileserver.localstorage.tmp # 本地文件存储的临时目录，默认值 /tmp

```

## 未来开发计划
1. 支持必要的安全认证
2. 未来图片标准化处理计划采用效率更高的ImageMagic，它能更好的支持图片合成，且性能更好。
3. 添加Word转PDF功能
4. 推出集群部署版本