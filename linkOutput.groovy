Closure c = { double inputLength ->
//Bar dimensions
//y normally 62
[barX = 10, barY = inputLength, barZ = 10]

//Screw dimensions
screwDiameter = 4.5;

//Set screw dimensions
setScrewDiameter = 3.7;

//Bolt transform closure
Closure boltTransform = (Closure)ScriptingEngine.gitScriptRun("https://gist.github.com/7ae5497be0aec76a07db.git", "servoBoltTransform.groovy", null);

//Output bar
CSG bar = new Cube(barX, barY, barZ).toCSG()
			.movey(5);

//Screw holes
CSG screwHoles = new Cylinder(screwDiameter / 2, screwDiameter / 2, 100, 30).toCSG()
			.movez(-20);
screwHoles = boltTransform(screwHoles).movey(inputLength / 3.5);

//Cut out screw holes
bar = bar.difference(screwHoles);

//Axle slot
CSG axleSlot = new Cube(3.175, 3.3, 100).toCSG()
			.movey(-inputLength / 2)
			.movez(-10)
			.makeKeepaway(0.8);

CSG wideAxleSlot = new Cube(3.175, 4, 100).toCSG()
			.movey(-inputLength / 2)
			.movez(-10)
			.makeKeepaway(0.8);

//Axle slot keyway
CSG keyway = new RoundedCube(10, 10, 10)
			.cornerRadius(2)
			.toCSG()
			.movey(-inputLength / 2);

//Cut out axle slot
keyway = keyway.difference(axleSlot);
keyway = keyway.difference(wideAxleSlot.roty(90));

//Set screw hole
CSG setScrewHole = new Cylinder(setScrewDiameter / 2, setScrewDiameter / 2, 5, 30).toCSG()
			.rotx(90)
			.movey(-36);

//Cut out set screw hole
keyway = keyway.difference(setScrewHole);

bar = bar.union(keyway);

//Alignment point
CSG line = new Cube(1, 35, 1).toCSG()
			.movex(5.4)
			.movey(-7)

bar = bar.union(line);
bar = bar.union(line.roty(90));
bar = bar.union(line.roty(180));

//return bar.movey(inputLength / 2 + 9.5 - 9.925).movez(37 - 5.942);
return bar.movey(inputLength / 2 - 0.75).movez(-5);
}

c(62)