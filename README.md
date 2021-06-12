# Notes for Localization for File

For localization, you can translate your files to the language you want, following the spelling convention in the example.

## Example usage

Add the following markers to the beginning of the text you want to translate

For example, we have a title feature

```yaml
title: Sample message
```
```yaml
x-title-i18n: Sample message
  x_tr: Örnek Mesaj
  x_zh: 样本信息
  x_ru: Образец сообщения
```

## Use for long translations



```java
title: |

    Long sample message

```
```java
«x»-title-i18n: |
    »x»
    Long sample message
    «x«
  «x»_tr: |
    »x»
    Uzun örnek mesaj
    «x«
  «x»_zh: |
    »x»
    长样本消息
    «x«
  «x»_ru: |
    »x»
    Длинный образец сообщения
    «X«
```

important in this use, use uppercase x when you get to the last dialing line. Like «X»

## Method use
If you want to use it as a method

```java

String yamlPath = "path/example.yaml";
yamlConfig.convertYaml("tr", yamlPath, "translatedYamlName");
```

## To run using JAR
```bash
java -jar sdbn-apis-localization-1.0.jar yamlName.yaml tr 
```
When you run the command, translated yaml will be created for you and it will generate translated yaml filename for you

File reading and saving path : "../build/api/"

yamlName_tr.yaml
