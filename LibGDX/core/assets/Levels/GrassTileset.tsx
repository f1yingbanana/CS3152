<?xml version="1.0" encoding="UTF-8"?>
<tileset name="tiles March22" tilewidth="450" tileheight="450" tilecount="40" columns="8">
 <image source="tiles March22.png" width="3619" height="2252"/>
 <terraintypes>
  <terrain name="grass" tile="-1"/>
 </terraintypes>
 <tile id="0" terrain=",,,0">
  <properties>
   <property name="Collision" value="top_left"/>
  </properties>
 </tile>
 <tile id="1" terrain=",,0,0">
  <properties>
   <property name="Collision" value="top"/>
  </properties>
 </tile>
 <tile id="2" terrain=",,0,">
  <properties>
   <property name="Collision" value="top_right"/>
  </properties>
 </tile>
 <tile id="8" terrain=",0,,0">
  <properties>
   <property name="Collision" value="left"/>
  </properties>
 </tile>
 <tile id="9" terrain="0,0,0,0"/>
 <tile id="10" terrain="0,,0,">
  <properties>
   <property name="Collision" value="right"/>
  </properties>
 </tile>
 <tile id="16" terrain=",0,,">
  <properties>
   <property name="Collision" value="bottom_left"/>
  </properties>
 </tile>
 <tile id="17" terrain="0,0,,">
  <properties>
   <property name="Collision" value="bottom"/>
  </properties>
 </tile>
 <tile id="18" terrain="0,,,">
  <properties>
   <property name="Collision" value="bottom_right"/>
  </properties>
 </tile>
 <tile id="24" terrain=",0,0,0">
  <properties>
   <property name="Collision" value="inside_top_left"/>
  </properties>
 </tile>
 <tile id="25" terrain="0,,0,0">
  <properties>
   <property name="Collision" value="inside_top_right"/>
  </properties>
 </tile>
 <tile id="32" terrain="0,0,,0">
  <properties>
   <property name="Collision" value="inside_bottom_left"/>
  </properties>
 </tile>
 <tile id="33" terrain="0,0,0,">
  <properties>
   <property name="Collision" value="inside_bottom_right"/>
  </properties>
 </tile>
</tileset>
