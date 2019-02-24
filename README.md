# PokemonTileMapper
Uses external controlsfx library

Can be download from Maven: org.controlsfx.control.GridView

In order to run project the following arg must be set on the VM:
*  --add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED

However I would recomend set all of thoose, in case of future development:
*  --add-exports=javafx.base/com.sun.javafx=ALL-UNNAMED
*  --add-exports=javafx.base/com.sun.javafx.collections=ALL-UNNAMED
*  --add-exports=javafx.graphics/com.sun.javafx.css=ALL-UNNAMED
*  --add-exports=javafx.graphics/com.sun.javafx.cursor=ALL-UNNAMED
*  --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED
*  --add-exports=javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED
*  --add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED
*  --add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
*  --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED
*  --add-exports=javafx.controls/com.sun.javafx.scene.control.skin=ALL-UNNAMED
*  --add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED
