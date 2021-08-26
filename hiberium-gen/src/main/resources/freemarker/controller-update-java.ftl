
    @RequestMapping(value = "/${concept_apipath}/{id}", method = RequestMethod.PUT)
    public ${concept_name} update${concept_name}(
            @PathVariable(value = "id") Integer ${concept_varname}Id,
            @Valid @RequestBody ${concept_name} ${concept_varname}Request,
            @RequestParam(value = "strategy", defaultValue = "Y") String strategy
            ) throws Exception {

        if (!${concept_varname}Id.equals(${concept_varname}Request.primaryKey()))
            throw new Exception("${concept_name} Id mismatch for put object");

        ${concept_varname}Id = service.handle${concept_name}InsertOrUpdate(${concept_varname}Request, strategy);
        return service.findByPrimaryKey(${concept_varname}Id);
    }

    @RequestMapping(value = "/${concept_apipath}/bulk", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> update${concept_name}Bulk(
            @Valid @RequestBody List<${concept_name}> ${concept_varname}RequestList,
            @RequestParam(value = "strategy", defaultValue = "B") String strategy
            ) throws Exception {

        service.handle${concept_name}InsertOrUpdate(${concept_varname}RequestList, strategy);
        return ResponseEntity.ok(true);
    }