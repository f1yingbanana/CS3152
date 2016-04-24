<?xml version="1.0" encoding="UTF-8"?>
<tileset name="newTile" tilewidth="128" tileheight="128" tilecount="72" columns="9">
 <image source="newTile.png" width="1153" height="1024"/>
 <terraintypes>
  <terrain name="New Terrain" tile="-1"/>
 </terraintypes>
 <tile id="9" terrain=",,,0">
  <properties>
   <property name="Collision" value="top_left"/>
  </properties>
 </tile>
 <tile id="10" terrain=",,0,0">
  <properties>
   <property name="Collision" value="top"/>
  </properties>
 </tile>
 <tile id="11" terrain=",,0,">
  <properties>
   <property name="Collision" value="top_right"/>
  </properties>
 </tile>
 <tile id="13" terrain=",,,0">
  <properties>
   <property name="Collision" value="top_left"/>
  </properties>
 </tile>
 <tile id="14" terrain=",,0,0">
  <properties>
   <property name="Collision" value="top"/>
  </properties>
 </tile>
 <tile id="15" terrain=",,0,0">
  <properties>
   <property name="Collision" value="top"/>
  </properties>
 </tile>
 <tile id="16" terrain=",,0,">
  <properties>
   <property name="Collision" value="top_right"/>
  </properties>
 </tile>
 <tile id="18" terrain=",0,,0">
  <properties>
   <property name="Collision" value="left"/>
  </properties>
 </tile>
 <tile id="19" terrain="0,0,0,0"/>
 <tile id="20" terrain="0,,0,">
  <properties>
   <property name="Collision" value="right"/>
  </properties>
 </tile>
 <tile id="22" terrain=",0,,0">
  <properties>
   <property name="Collision" value="left"/>
  </properties>
 </tile>
 <tile id="23" terrain="0,0,0,0"/>
 <tile id="24" terrain="0,0,0,0"/>
 <tile id="25" terrain="0,,0,">
  <properties>
   <property name="Collision" value="right"/>
  </properties>
 </tile>
 <tile id="27" terrain=",0,,">
  <properties>
   <property name="Collision" value="bottom_left"/>
  </properties>
 </tile>
 <tile id="28" terrain="0,0,,">
  <properties>
   <property name="Collision" value="bottom"/>
  </properties>
 </tile>
 <tile id="29" terrain="0,,,">
  <properties>
   <property name="Collision" value="bottom_right"/>
  </properties>
 </tile>
 <tile id="31" terrain=",0,,0">
  <properties>
   <property name="Collision" value="left"/>
  </properties>
 </tile>
 <tile id="32" terrain="0,0,0,0"/>
 <tile id="33" terrain="0,0,0,0"/>
 <tile id="34" terrain="0,,0,">
  <properties>
   <property name="Collision" value="right"/>
  </properties>
 </tile>
 <tile id="40" terrain=",0,,">
  <properties>
   <property name="Collision" value="bottom_left"/>
  </properties>
 </tile>
 <tile id="41" terrain="0,0,,">
  <properties>
   <property name="Collision" value="bottom"/>
  </properties>
 </tile>
 <tile id="42" terrain="0,0,,">
  <properties>
   <property name="Collision" value="bottom"/>
  </properties>
 </tile>
 <tile id="43" terrain="0,,,">
  <properties>
   <property name="Collision" value="bottom_right"/>
  </properties>
 </tile>
 <tile id="45" terrain=",0,0,0">
  <properties>
   <property name="Collision" value="inside_top_left"/>
  </properties>
 </tile>
 <tile id="46" terrain="0,,0,0">
  <properties>
   <property name="Collision" value="inside_top_right"/>
  </properties>
 </tile>
 <tile id="54" terrain="0,0,,0">
  <properties>
   <property name="Collision" value="inside_bottom_left"/>
  </properties>
 </tile>
 <tile id="55" terrain="0,0,0,">
  <properties>
   <property name="Collision" value="inside_bottom_right"/>
  </properties>
 </tile>
 <tile id="58">
  <properties>
   <property name="angle" value="NORTH"/>
   <property name="level" value="1"/>
   <property name="mType" value="UNDER"/>
   <property name="span" value="100"/>
   <property name="spcf" value="0.25"/>
   <property name="type" value="undermonster"/>
  </properties>
 </tile>
 <tile id="67">
  <properties>
   <property name="type" value="ship"/>
  </properties>
 </tile>
 <tile id="68">
  <properties>
   <property name="angle" value="NORTH"/>
   <property name="initMove" value="RIGHT"/>
   <property name="level" value="1"/>
   <property name="mType" value="OVER"/>
   <property name="span" value="100"/>
   <property name="spcf" value="0.4"/>
   <property name="type" value="overmonster"/>
  </properties>
 </tile>
 <tile id="69">
  <properties>
   <property name="type" value="player"/>
  </properties>
 </tile>
 <tile id="70">
  <properties>
   <property name="type" value="gate"/>
  </properties>
 </tile>
</tileset>
