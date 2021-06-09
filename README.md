# VKGL data sharing of variant classifications: consensus

Command-line application to generate consensus file for VKGL data sharing of variant classifications
based on the outputs of https://github.com/molgenis/data-transform-vkgl.

## Requirements

- Java 11

## Usage

```
java -jar vkgl-consensus.jar --amc <arg> --erasmus_mc <arg> --lumc <arg> --nki <arg>
                             --radboud_mumc <arg> --umc_utrecht <arg> --umcg <arg> --vumc <arg>
                             -o <arg>
-a,--amc <arg>            Required: transformed AMC lab data (.tsv).
-e,--erasmus_mc <arg>     Required: transformed Erasmus MC lab data (.tsv).
-l,--lumc <arg>           Required: transformed LUMC lab data (.tsv).
-n,--nki <arg>            Required: transformed NKI lab data (.tsv).
-r,--radboud_mumc <arg>   Required: transformed Radboud MUMC lab data (.tsv).
-u,--umc_utrecht <arg>    Required: transformed UMC Utrecht lab data (.tsv).
-g,--umcg <arg>           Required: transformed UMCG lab data (.tsv).
-v,--vumc <arg>           Required: transformed VUMC lab data (.tsv).
-o,--output <arg>         Required: output consensus data (.tsv)
-f,--force                Overwrite output file if it already exists.
```
