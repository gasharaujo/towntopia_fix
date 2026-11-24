# Gash's TownTopia Fix

Addon para **Minecraft 1.21.1**, **NeoForge 21.1.238+** e **TownTopia 0.1.2 ou superior**. O TownTopia também exige GeckoLib 4.8 ou superior na série 4.x.

## Instalação

Substitua a versão anterior por `gashs-towntopia-fix-neoforge-1.21.1-1.1.1.jar` na pasta `mods`, junto com o TownTopia e o GeckoLib. Para um servidor dedicado, instale o addon também nos clientes.

## TownKit

O [TownKit](https://github.com/y33tysk33ty/townkit-mod) é opcional. As bancadas reconhecidas pelo addon funcionam junto com as substituições de estações registradas no TownKit. Os minérios da tag `c:ores` também continuam reconhecidos junto com os minérios registrados pelo TownKit. O TownKit 0.8.1 requer TownTopia 0.1.3 ou superior.

## Refined Storage e Applied Energistics 2

As duas integrações vêm desligadas. Abra um mundo uma vez para gerar `config/gashs_towntopia_fix-server.toml` na instância. Feche o mundo ou pare o servidor, ajuste as opções desejadas e abra o mundo novamente. Para configurar só um mundo, coloque uma cópia do arquivo na pasta `serverconfig` desse mundo.

```toml
[storage]
refinedStorage = true
appliedEnergistics = true
```

Coloque uma **Interface** do Refined Storage ou uma **ME Interface** do AE2 em um Armazém do TownTopia, numa posição acessível aos aldeões. O AE2 aceita a interface em bloco ou no cabo. Com a interface ativa e conectada, os aldeões podem consultar, retirar e depositar **itens** no estoque da rede, conforme as regras do Armazém. Deixe em `false` a opção correspondente a cada integração que quiser manter desligada.

## Compatibilidade

| Categoria | O que o addon reconhece |
| --- | --- |
| Armazenamento | Baús e barris pelas tags de bloco ou item, armários do Farmer's Delight, gavetas locais de itens do Functional Storage e gavetas, armários, caixotes e coolers do Refurbished Furniture. |
| Móveis | O mapa do marcador da casa desenha os novos armazenamentos e móveis do Refurbished Furniture, como cadeiras, mesas e sofás, quando eles estão junto ao piso reconhecido do cômodo. |
| Bancadas | Variantes de `CraftingTableBlock` e bancadas nas tags `c:crafting_tables`, `c:workbenches` ou `gashs_towntopia_fix:crafting_tables`. Contam como bancadas no Armazém e na Cozinha. |
| Plantações | Plantas modded baseadas em `CropBlock` cujo item de plantio coloca o bloco da cultura. Os agricultores usam o ciclo de plantio, crescimento e colheita do TownTopia em terra arada. |
| Ferramentas | Picaretas, machados, enxadas e espadas nas tags da categoria, além das tesouras de colheita marcadas em `towntopia:farmer_shears`. As ferramentas modded entram no nível vanilla equivalente pela durabilidade, com o material de ouro reconhecido separadamente. |
| Minérios | O minerador identifica blocos em `c:ores` nos túneis e veios e usa a tabela de loot do bloco. As receitas do ferreiro e os minérios bônus gerados pelo TownTopia seguem as regras do mod base. |

O menu do agricultor usa as mesmas preferências de plantio para as sementes detectadas. Culturas com solos, idades ou colheitas especiais precisam de uma definição específica. O arroz e as trepadeiras do Farmer's Delight têm mecânicas próprias.

## Adicionar blocos e itens por datapack

- Bancadas podem entrar em `data/gashs_towntopia_fix/tags/block/crafting_tables.json`.
- Armazenamento pode entrar em `data/towntopia/tags/block/storage_containers.json`.
- Ferramentas podem entrar nas tags de item `minecraft:pickaxes`, `minecraft:axes`, `minecraft:hoes` e `minecraft:swords`. Tesouras de colheita usam `towntopia:farmer_shears`.
- Minérios podem entrar em `data/c/tags/block/ores.json`.
- Definições de plantações usam `data/<namespace>/farmer_crops/<cultura>.json` no formato do TownTopia. Para um exemplo, consulte `data/towntopia/farmer_crops/wheat.json` dentro do JAR do TownTopia.

Use `"replace": false` nas tags para adicionar entradas às que o modpack já fornece.

## Compilação

Com o JDK 21, execute `./gradlew.bat build` no Windows ou `./gradlew build` no Linux. O Gradle resolve as dependências de compilação pelo Curse Maven. Para usar JARs locais, passe os caminhos nas propriedades `townTopiaJar`, `refinedStorageJar` e `appliedEnergisticsJar`.
