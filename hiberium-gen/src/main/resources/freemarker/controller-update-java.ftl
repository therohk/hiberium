
    @RequestMapping(value = "/${concept_apipath}/{id}", method = RequestMethod.PUT)
    public ResponseEntity<${concept_name}> update${concept_name}(
            @PathVariable(value = "id") Integer ${concept_varname}Id,
            @Valid @RequestBody ${concept_name} ${concept_varname}Request,
            @RequestParam(value = "strategy", defaultValue = "Y") String strategy
            ) throws Exception {

        if (!${concept_varname}Id.equals(${concept_varname}Request.primaryKey()))
            throw new Exception("${concept_name} Id mismatch for put object");

        ${concept_name} ${concept_varname} = repository.getOne(${concept_varname}Id);
        if (${concept_varname} == null)
            throw new Exception("${concept_name} " + ${concept_varname}Id + " not found");

        ${concept_varname}.handleFieldsForUpdate(${concept_varname}Request);
        final ${concept_name} ${concept_varname}Updated = repository.save(${concept_varname});

        log.info("UPDATE ${concept_name} where id={}", ${concept_varname}Id);
        return ResponseEntity.ok(${concept_varname}Updated);
    }