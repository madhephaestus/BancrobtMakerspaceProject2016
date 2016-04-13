import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import com.neuronrobotics.bowlerstudio.physics.*;

	/**
 * Gets the all dh chains.
 *
 * @return the all dh chains
 */
public ArrayList<DHParameterKinematics> getLimbDHChains(MobileBase base) {
	ArrayList<DHParameterKinematics> copy = new ArrayList<DHParameterKinematics>();
	for(DHParameterKinematics l:base.getLegs()){
		copy.add(l);	
	}
	for(DHParameterKinematics l:base.getAppendages() ){
		copy.add(l);	
	}
	return copy;
}
println "Loading STL file"
// Load an STL file from a git repo
// Loading a local file also works here
		CSG dyioReference=   (CSG)(ScriptingEngine.inlineGistScriptRun(
		"fb4cf429372deeb36f52", 
		"dyioCad.groovy" ,
		null))
	
//	.transformed(new Transform().translateZ(12.0))
//	.transformed(new Transform().translateX(5.4));
	
	//CSG horn=  STL.file(NativeResource.inJarLoad(IVitamin.class,"smallmotorhorn.stl").toPath())
	CSG horn = new Cube(6,5,12).toCSG();
	CSG hornBlank = new Cube(6,5,12).toCSG()
			.toZMax()
			.toYMin()
			.movez(4)
	CSG mountReference = (CSG)(ScriptingEngine.inlineGistScriptRun(
		"ce4e7c95d516e265b91e",
		"servoAttachment.groovy" ,
		[hornBlank]))
	CSG mountScrewKeepaway = (CSG)(ScriptingEngine.inlineGistScriptRun(
		"488e0ee249a5c16ae4d8",
		"moutScrewKeepaway.groovy" ,
		null))
// Load the .CSG from the disk and cache it in memory
//CSG servo  = Vitamins.get(servoFile);
return new ICadGenerator(){
	@Override 
	public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
		ArrayList<CSG> allCad=new ArrayList<>()

		return allCad;
	}
	private CSG getAppendageMount(){

		
		return new Cube(20).toCSG();
	}
	private CSG getAttachment(){

		
		return new Cube(10).toCSG();
	}
	@Override 
	public ArrayList<CSG> generateBody(MobileBase b ) {
		ArrayList<CSG> allCad=new ArrayList<>();
		ArrayList<CSG> cutouts=new ArrayList<>();
		ArrayList<CSG> attach=new ArrayList<>();
		CSG attachUnion=null;
		for(DHParameterKinematics l:b.getLegs()){
			TransformNR position = l.getRobotToFiducialTransform();
			Transform csgTrans = TransformFactory.nrToCSG(position)
			cutouts.add(getAppendageMount()
				.transformed(csgTrans)
				);
			CSG attachment = getAttachment()
				.transformed(csgTrans)
			attach.add(attachment);
			if(attachUnion==null){
				attachUnion=attachment;
			}else{
				attachUnion = 	attachUnion.union(attachment)
			}
			
		}

		CSG upperBody = attachUnion.hull()
		
		
						
		CSG myDyIO=dyioReference
				.movez(upperBody.getMaxZ()+22.0)
				.movex(	upperBody.getMaxX()-
						Math.abs(upperBody.getMinX()))
				.movey(	upperBody.getMaxY()-
						Math.abs(upperBody.getMinY()))
		upperBody=upperBody
		.union(myDyIO)
		.hull()
		.difference(myDyIO)
		
		upperBody= upperBody.difference(cutouts);
		
		upperBody= upperBody.union(attach);
		
	
					
		
		upperBody.setManipulator(b.getRootListener());
		upperBody.setManufactuing(new PrepForManufacturing() {
					public CSG prep(CSG arg0) {
						return arg0.toZMin();
					}
				});
		allCad.add(upperBody)
		
		
		return allCad;
	}
};
