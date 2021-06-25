
    @RequestMapping(value = "/${concept_apipath}/{id}", method = RequestMethod.PUT)
    public ResponseEntity<${concept_name}> update${concept_name}(
            @PathVariable(value = "id") Integer ${concept_varname}Id,
            @Valid @RequestBody ProductFaq ${concept_varname}Request)
            throws Exception {

        if (!${concept_varname}Id.equals(${concept_varname}Request.getProductFaqId()))
            throw new Exception("Product Tax Id mismatch for put object");

        ${concept_name} ${concept_varname} = repository.getOne(${concept_varname}Id);
        if (${concept_varname} == null)
            throw new Exception("Product Tax " + ${concept_varname}Id + " not found");

        ${concept_varname}.setFieldsForUpdate(${concept_varname}Request);
        final ${concept_name} ${concept_varname}Updated = repository.save(${concept_varname});

        LOG.info("UPDATE ${concept_name} where id=" + ${concept_varname}Id);
        return ResponseEntity.ok(${concept_varname}Updated);
    }
