return { double inputLength ->
//Bar dimensions
//y normally 62
[barX = 10, barY = inputLength, barZ = 10]

//Screw dimensions
screwDiameter = 4.5;

//Bolt transform closure
Closure boltTransform = (Closure)ScriptingEngine.gitScriptRun("https://gist.github.com/7ae5497be0aec76a07db.git", "servoBoltTransform.groovy", null);

//Output bar
CSG bar = new Cube(barX, barY, barZ).toCSG()
			.movey(5);

//Screw holes
CSG screwHoles = new Cylinder(screwDiameter / 2, screwDiameter / 2, 100, 30).toCSG()
			.movez(-20);
screwHoles = boltTransform(screwHoles).movey(17);

//Cutout screw holes
bar = bar.difference(screwHoles);

//Axle slot
CSG axleSlot = new Cube(3.175, 3.3, 100).toCSG()
			.movey(-30)
			.movez(-10)
			.makeKeepaway(0.8);

CSG wideAxleSlot = new Cube(3.175, 4, 100).toCSG()
			.movey(-30)
			.movez(-10)
			.makeKeepaway(0.8);

//Axle slot keyway
CSG keyway = new RoundedCube(10, 10, 10)
			.cornerRadius(2)
			.toCSG()
			.movey(-30);

//Cut out axle slot
keyway = keyway.difference(axleSlot);
keyway = keyway.difference(wideAxleSlot.roty(90));

bar = bar.union(keyway);

//Alignment point
CSG line = new Cube(1, 35, 1).toCSG()
			.movex(5.4)
			.movey(-7)

bar = bar.union(line);
bar = bar.union(line.roty(90));
bar = bar.union(line.roty(180));

return bar.movey(inputLength / 2 + 9.5).movez(37);
}
