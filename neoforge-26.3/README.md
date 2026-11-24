# Minecraft 26.3

Use Java 25 e NeoForge 26.3.0.113. Quando houver um JAR do TownTopia para NeoForge 26.3, rode neste diretório:

```powershell
.\gradlew.bat build '-PtownTopiaJar=C:\caminho\towntopia.jar'
```

Confira os alvos e descritores dos mixins com o JAR publicado. Para compilar o adaptador de estoque do Refined Storage, passe também `-PrefinedStorageJar` com o JAR para 26.3. Para rodar o ambiente de desenvolvimento, `-PgeckoLibJar`, `-PappliedEnergisticsJar` e `-PguideMeJar` aceitam os caminhos dos JARs para 26.3.
