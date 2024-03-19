package co.oslc.refimpl.rm.gen.services;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RequirementCreatorHelper {
	
	public List<Requirement>  parseReqJson(String jsonString) {
		JSONParser parser = new JSONParser();
		List<Requirement> requirementList = new ArrayList<>();
		try {
			JSONArray reqList = (JSONArray) parser.parse(jsonString);
			reqList.forEach(req -> requirementList.add(parseRequirementObject((JSONObject) req)));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return requirementList;
	}

	public List<Requirement> jsonRequirement() {
		List<Requirement> requirementList = new ArrayList<>();
		InputStream ioStream = this.getClass().getClassLoader().getResourceAsStream("requirements.json");

		JSONParser jsonParser = new JSONParser();
		try (InputStreamReader reader = new InputStreamReader(ioStream)) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONArray reqList = (JSONArray) obj;
			reqList.forEach(req -> requirementList.add(parseRequirementObject((JSONObject) req)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return requirementList;
	}

	private Requirement parseRequirementObject(JSONObject req) {
		JSONObject reqObject = (JSONObject) req.get("requirement");
		String id = (String) reqObject.get("id");
		String title = (String) reqObject.get("title");
		String description = (String) reqObject.get("description");

		return createRequirement(id, title, description);
	}

	public Requirement createRequirement(String id, String title, String description) {
		Requirement r = new Requirement();
		r.setIdentifier("req_" + id);
		r.setTitle(title);
		r.setShortTitle("sp_single-R" + id);
		r.setDescription(description);
		r.setModified(new Date());
		r.setConstrainedBy(randomRequirementLink(20, Integer.parseInt(id)));
		r.setElaboratedBy(randomRequirementLink(20, Integer.parseInt(id)));
		return r;
	}

	private Set<Link> randomRequirementLink(int maxId, int exceptId) {
		Set<Link> retValueSet = new HashSet<>();
		String to = "http://10.224.180.22:8800/services/Requirement/sp_single/req_" + exceptId;
		retValueSet.add(new Link(URI.create(to)));
		return retValueSet;
	}

}
