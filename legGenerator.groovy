import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import java.util.*
//println "Loading STL file"
// Load an STL file from a git repo
// Loading a local file also works here
//File servoFile = ScriptingEngine.fileFromGit(
//	"https://github.com/NeuronRobotics/BowlerStudioVitamins.git",
//	"BowlerStudioVitamins/stl/servo/smallservo.stl");

ScriptingEngine.setAutoupdate(true)

Closure generateLink = (Closure)ScriptingEngine
					 .gitScriptRun(
            "https://github.com/madhephaestus/BancroftMakerspaceProject2016.git", // git location of the library
            "linkOutput.groovy" , // file to load
            null// no parameters (see next tutorial)
            );

Closure generateServo = (Closure)ScriptingEngine
					 .gitScriptRun(
            "https://github.com/madhephaestus/BancroftMakerspaceProject2016.git", // git location of the library
            "vexServo.groovy" , // file to load
            null// no parameters (see next tutorial)
            );

Closure generateProtractor = (Closure)ScriptingEngine
					 .gitScriptRun(
            "https://github.com/madhephaestus/BancroftMakerspaceProject2016.git", // git location of the library
            "servoProtractor.groovy" , // file to load
            null// no parameters (see next tutorial)
            );
            
// Load the .CSG from the disk and cache it in memory
//CSG servo  = Vitamins.get(servoFile);

//Cache parts
CSG servoCache = generateServo(30);
CSG protractorCache = generateProtractor();
Map<Float, CSG> linkCache = new HashMap<Float, CSG>();

return new ICadGenerator(){
	@Override 
	public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
		ArrayList<DHLink> dhLinks = d.getChain().getLinks()
		ArrayList<CSG> allCad=new ArrayList<>()
		return allCad
		int i=linkIndex;
		
//		println "Generating link: "+linkIndex
		
		DHLink dh = dhLinks.get(linkIndex)
		// Hardware to engineering units configuration
		LinkConfiguration conf = d.getLinkConfiguration(i);
		// Engineering units to kinematics link (limits and hardware type abstraction)
		AbstractLink abstractLink = d.getAbstractLink(i);// Transform used by the UI to render the location of the object
		// Transform used by the UI to render the location of the object
		Affine manipulator = dh.getListener();
//		CSG tmpSrv = servo.clone()

//		println "Making servo"
		CSG tmpSrv = servoCache
					.movez(-dh.getD())
					.rotz(-Math.toDegrees(dh.getTheta()))
					.rotz(90 + Math.toDegrees(dh.getTheta()))
					.movex(-dh.getR())
					.rotx(Math.toDegrees(dh.getAlpha()))
		tmpSrv.setManipulator(manipulator)
		allCad.add(tmpSrv)

		//println "Making bar"
		if (!linkCache.keySet().contains(dh.getR()))
		{
			println "New bar"
			linkCache.put(dh.getR(), generateLink(dh.getR()));
		}
		CSG tmpLink = linkCache.get(dh.getR())
					.movez(-dh.getD())
					.rotz(-Math.toDegrees(dh.getTheta()))
					.rotz(90 + Math.toDegrees(dh.getTheta()))
					.movex(-dh.getR())
					.rotx(Math.toDegrees(dh.getAlpha()))
		tmpLink.setManipulator(manipulator)
		allCad.add(tmpLink)

//		println "Making protractor"
		CSG tmpProtractor = protractorCache
					.movez(-dh.getD())
					.rotz(-Math.toDegrees(dh.getTheta()))
					.rotz(90 + Math.toDegrees(dh.getTheta()))
					.movex(-dh.getR())
					.rotx(Math.toDegrees(dh.getAlpha()))
		tmpProtractor.setManipulator(manipulator)
		allCad.add(tmpProtractor)

		println "Finished"

//		if(i==0){
//			// more at https://github.com/NeuronRobotics/java-bowler/blob/development/src/main/java/com/neuronrobotics/sdk/addons/kinematics/DHLink.java
//			println dh
//			println "D = "+dh.getD()// this is the height of the link
//			println "R = "+dh.getR()// this is the radius of rotation of the link
//			println "Alpha = "+Math.toDegrees(dh.getAlpha())// this is the alpha rotation
//			println "Theta = "+Math.toDegrees(dh.getTheta())// this is the rotation about hte final like orentation
//			println conf // gets the link hardware map from https://github.com/NeuronRobotics/java-bowler/blob/development/src/main/java/com/neuronrobotics/sdk/addons/kinematics/LinkConfiguration.java
//			println conf.getHardwareIndex() // gets the link hardware index
//			println conf.getScale() // gets the link hardware scale to degrees from link units
//			// more from https://github.com/NeuronRobotics/java-bowler/blob/development/src/main/java/com/neuronrobotics/sdk/addons/kinematics/AbstractLink.java
//			println  "Max engineering units for link = " + abstractLink.getMaxEngineeringUnits() 
//			println  "Min engineering units for link = " + abstractLink.getMinEngineeringUnits() 
//			println "Position "+abstractLink.getCurrentEngineeringUnits()
//			println manipulator
//		}
		return allCad;
	}
	@Override 
	public ArrayList<CSG> generateBody(MobileBase b ) {
		ArrayList<CSG> allCad=new ArrayList<>();

		return allCad;
	}
};
