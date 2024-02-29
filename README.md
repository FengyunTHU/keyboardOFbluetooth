# keyboard

#### 介绍

Android Studio开发的模拟蓝牙键盘

#### 软件架构

软件架构说明

#### 安装教程

1. xxxx
2. xxxx
3. xxxx

#### 使用说明

1. xxxx
2. xxxx
3. xxxx

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

#### Attentions

1. 使用 `Readme_XXX.md` 来支持不同的语言，例如 `Readme_en.md // 暂无`, `Readme_zh.md`
2. 导入图片支持的格式有：

   - 传统位图格式：`.png,.jpeg,.bmp`;
   - 矢量图格式（推荐）：`.svg(.xml)`;
   - 其他图像格式：`.gif,.webp,.heif`;
3. 导入键位信息文件标准：

   - 使用 `.txt/.csv`格式导入;
   - `.txt/.csv`文件的内容结构标准示例（注释不需要包含）：

   ```txt
   82  // 总键位数
   esc,15,13,10    // 键位名称+相对于图片原尺寸的坐标
   f1,45,12,11
   f2,77,14,12
   f3,105,12,12
   ……  ……  ……  ……  // 此处省略其他键位信息
   ctrl,379,161,413,188
   left,416,161,446,188
   down,449,161,477,188
   right,481,160,510,189
   ```

   - 提供键位的位置信息数字即可，如 `15,13,10`。会自动依据所给的数字个数分配 `area.shape`，包含 `=3->circle(圆),=4->rect(矩形),≥5->poly(多边形)`。推荐使用网站 <a href="https://www.image-map.net">image map generator </a>；

#### 开发历程

1. `2024/2/24:`软件在 `Android`上报错了…
2. `2024/2/28:`代码中包含很多的 `Log.d(TAG,"String")`来输出日志信息以便于修改错误；
