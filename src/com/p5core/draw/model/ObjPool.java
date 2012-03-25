package com.p5core.draw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.PApplet;
import saito.objloader.OBJModel;
import toxi.geom.mesh.WETriangleMesh;

import com.p5core.draw.util.ThreeDeeUtil;

/**
 * ObjPool is a convenient way to load a bunch of .obj files and have a toxiclibs WETriangleMesh of each, always ready to be copied or used for different purposes.
 * @author cacheflowe
 *
 */
public class ObjPool {
	
	protected PApplet p;
	protected HashMap<String, ObjItem> _models;
	
	public ObjPool( PApplet p ) {
		this.p = p;
		_models = new HashMap<String, ObjItem>();
	}
	
	public void loadObj( String id, float scale, String file ) {
		_models.put( id, new ObjItem( p, scale, file ) );
	}
		
	public WETriangleMesh getMesh( String id ) {
		return _models.get( id )._mesh;
	}

	public OBJModel getModel( String id ) {
		return _models.get( id )._obj;
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
		public OBJModel _obj;
		public WETriangleMesh _mesh;
		
		/**
		 * Initializes
		 * @param p
		 * @param scale
		 * @param file
		 */
		public ObjItem( PApplet p, float scale, String file ) {
			_file = file;
			_scale = scale;
			_obj = new OBJModel( p, file );
			_obj.scale( _scale );
			_mesh = ThreeDeeUtil.ConvertObjModelToToxiMesh( p, _obj );
		}
	}
}
