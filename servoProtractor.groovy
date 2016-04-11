return {
double bodyZ = 38.6;
double bodyX = 40;
double bodyY = 20;

double bottomPillar = 8.7;
double topPillar = 8.1;

double supportYCross = 7.8;
double supportXCross = 12;
double pillarHeight = 14.9;
double crossHeight = 12;
double screwTopDiameter = 5;
double screwTopHeight = 1;

double distanceBetweenPillars = 12.4;
double largeShaftCircle = 14;
double screwHoleDiameter = 4.4;
double screwLength = 12;

double shaftSize = 3.6;
double shaftSize2 = 3.3;

/////This is part of the Cube bottom link///////////////////////////////////////////////////////////
double bodyXx = 36;
double bodyYy = 12;  
double bodyZz = 5;

double lengthBetweenScrews2 = 12.4
double screwHoleDiameter2 = 4.4
double screwHeight2 = 50;
double vexHoleLength = 15;
//////////////////////////////////////////////////////////////////////////////////////////////////////



CSG holeDiameter = new Cylinder(screwHoleDiameter/2, //radius at bottom
					       screwHoleDiameter/2, //radius at top
					       screwLength, //height
				           (int)30
				           ).toCSG()	

CSG shaft = new Cube(shaftSize2, //x
				shaftSize2, //y
				10 //z
				).toCSG()

CSG shaft2 = new Cube(shaftSize2, //x
				shaftSize2, //y
				120 //z
				).toCSG()
							         
//////    This is where the Cube bottom is located    /////////////////////////////////////////////////////////////
				           				  		  				      				
CSG linkBody = new RoundedCube(bodyXx, //X
				bodyYy, //y
				bodyZz //z
				).cornerRadius(2).toCSG()

CSG screwHoles = new Cylinder(screwHoleDiameter2/2,
						screwHoleDiameter2/2,
						30,
						(int)30
						).toCSG()

CSG screwHoles3 = new Cylinder(2.3,
						 2.3,
						3,
						(int)30
						).toCSG()

CSG screwHoles4 = new Cylinder(3,
						 3,
						3,
						(int)30
						).toCSG()						
						
CSG sphere = new Sphere(17)
					.toCSG()

CSG cubeCutter = new Cube(40, //x
					 40, //y
					 20 //z
					 ).toCSG()

CSG degreeMarker1 = new RoundedCube(12, //x
					    3, //y
					    3 //z
					   ).cornerRadius(0.5).toCSG()

CSG degreeMarker2 = new RoundedCube(3, //x
					    12.4, //y
					    3 //z
					   ).cornerRadius(0.5).toCSG()	

CSG pillarTop = new Cylinder(2.8, //top
					    2.8, //bottom
					    0.9, //height
					    (int)30
					    ).toCSG()				   				   

CSG cutSphere1 = sphere.difference(cubeCutter.movez(12.5))
CSG cutSphere2 = cutSphere1.difference(cubeCutter.movez(-12.5))  
CSG cutSphere3 = cutSphere2.difference(degreeMarker1.movez(2).movex(-10))
CSG cutSphere4 = cutSphere3.difference(degreeMarker2.movey(-10).movez(1)).difference(degreeMarker2.movey(10).movez(1))
//CSG cutSphere4 = cutSphere3.union(degreeMarker2.movey(-10).movez(2.5)).union(degreeMarker2.movey(10).movez(2.5))
CSG protractor = cutSphere4
////////////////////////////////////////////////////////////////////////////////////////////////////////////
CSG topCup = screwHoles4.difference(screwHoles3)

////////////////////////////////////////////////////////////////////////////////////////////////////////////

CSG screwHole = screwHoles//.union(pillarTop.movez(6.6))
CSG linkSphere1 = linkBody.movex(12.4).union(protractor)
CSG linkSphere2 = linkSphere1.difference(screwHole.movex(lengthBetweenScrews2 + 12.4).movez(-5))
CSG linkSphere3 = linkSphere2.difference(screwHole.movex(12.4).movez(-5))
CSG linkSphere = linkSphere3.difference(screwHoles3)
 

return linkSphere.movey(71.7)
}
