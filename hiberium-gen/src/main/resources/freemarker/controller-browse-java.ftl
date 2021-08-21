
    @RequestMapping(value = "/${concept_apipath}/page/{pageNum}", method = RequestMethod.POST)
    public List<${concept_name}> browse${concept_name}BySample(
            @RequestBody(required = false) ${concept_name} ${concept_varname}Sample,
            @PathVariable(value = "pageNum") Integer pageNum,
            @RequestParam(value = "perPage", defaultValue = "20") Integer perPage,
            @RequestParam(value = "sortField", required = false) List<String> sortFields,
            @RequestParam(value = "sortAsc", defaultValue = "true") Boolean sortAsc
            ) throws Exception {

        return service.searchByExample(${concept_varname}Sample, pageNum, perPage, sortFields, sortAsc);
    }