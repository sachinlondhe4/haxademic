package com.haxademic.core.draw.mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.PApplet;
import saito.objloader.OBJModel;
import toxi.geom.mesh.WETriangleMesh;

import com.haxademic.core.draw.util.ThreeDeeUtil;

/**
 * ObjPool is a convenient way to load a bunch of .obj files and have a toxiclibs WETriangleMesh of each, always ready to be copied or used for different purposes.
 * @author cacheflowe
 *
 */
public class MeshPool {
	
	protected PApplet p;
	protected HashMap<String, ObjItem> _models;
	
	public MeshPool( PApplet p ) {
		this.p = p;
		_models = new HashMap<String, ObjItem>();
	}
	
	public void loadObj( String id, float scale, String file ) {
		// load and scale the .obj file. convert to mesh and store it 
		OBJModel obj = new OBJModel( p, file, OBJModel.RELATIVE );
		obj.scale( scale );
		WETriangleMesh mesh = ThreeDeeUtil.ConvertObjModelToToxiMesh( p, obj );
		_models.put( id, new ObjItem( p, scale, file, mesh ) );
	}
		
//	public void loadSVG( String id, float scale, String file ) {
//		_models.put( id, new ObjItem( p, scale, file ) );
//	}
		
	public WETriangleMesh getMesh( String id ) {
		return _models.get( id )._mesh;
	}

	public OBJModel getModel( String id ) {
		return null;//_models.get( id )._obj;
	}

	public ArrayList<String> getIds() {
		ArrayList<String> keyList = new ArrayList<String>();
		Iterator iter = _models.keySet().iterator();
	    while (iter.hasNext()) {
	    	keyList.add( iter.next().toString() );
	    	System.out.println("Loaded model: "+keyList.get( keyList.size()-1 ));
	    }
		return keyList;
	}

	/**
	 * ObjItem is used to initialize a model with a base scale, since we might not always be able to normalize the model in Blender, etc.
	 * @author cacheflowe
	 *
	 */
	public class ObjItem {
		public String _file;
		public float _scale;
		public WETriangleMesh _mesh;
		
		/**
		 * Initializes
		 * @param p
		 * @param scale
		 * @param file
		 */
		public ObjItem( PApplet p, float scale, String file, WETriangleMesh mesh ) {
			_file = file;
			_scale = scale;
			_mesh = mesh;
		}
	}
}
