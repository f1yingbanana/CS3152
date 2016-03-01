Shader "Custom/MetaballWater" {
  Properties {    
    _MainTex ("Texture", 2D) = "white" {}
    _MainColor ("Main Color", Color) = (0.2,0.5,0.9,0.5)
    _RimColor ("Rim Color", Color) = (0.1,0.25,0.45,0.5)
  }
	
	SubShader {
		Tags {"Queue" = "Transparent" }
    
    Pass {
    	Blend SrcAlpha OneMinusSrcAlpha     
	
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag	
			#include "UnityCG.cginc"
			float4 _MainColor;
			float4 _RimColor;
			sampler2D _MainTex;

			struct v2f {
	    	float4  pos : SV_POSITION;
	    	float2  uv : TEXCOORD0;
			};

			float4 _MainTex_ST;		
			
			v2f vert (appdata_base v){
	    	v2f o;
	    	o.pos = mul (UNITY_MATRIX_MVP, v.vertex);
	    	o.uv = TRANSFORM_TEX (v.texcoord, _MainTex);
	    	return o;
			}	

			// Here goes the metaball magic
			half4 frag (v2f i) : COLOR{		
				half4 texcol = tex2D(_MainTex, i.uv); 
				half4 finalColor = texcol;

				if (texcol.b > 0.5) {
	    		finalColor = _MainColor;
				} else if (texcol.b > 0.3) {
					finalColor = _RimColor;
				} else {
					finalColor = float4(0, 0, 0, 0);
				}

	    	return finalColor;
			}
			
			ENDCG
    }
}
Fallback "VertexLit"
} 