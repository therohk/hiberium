
    @Deprecated //use rsql-searchByQuery or browseBySample
    @RequestMapping(value = "/${concept_apipath}/find/{label}/{value}", method = RequestMethod.GET)
    public ResponseEntity<List<${concept_name}>> find${concept_name}BySearchField(
            @PathVariable(value = "label") String searchLabel,
            @PathVariable(value = "value") String searchValue,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "perPage", defaultValue = "10") Integer perPage
            ) throws Exception {

        List<${concept_name}> ${concept_varname}List = new ArrayList<${concept_name}>();
        switch (searchField) {
<#list attributes as attribute>
    <#if !attribute.attribute_role?contains("R")><#continue></#if>
            case "${attribute.attribute_name}":
    <#if attribute.attribute_java == "String">
                ${concept_varname}List = repository.findBy${attribute.attribute_name?cap_first}(searchLabel);
    <#elseif attribute.attribute_java == "Integer">
                ${concept_varname}List = repository.findBy${attribute.attribute_name?cap_first}(Integer.parseInt(searchLabel));
    <#elseif attribute.attribute_java == "Long">
                ${concept_varname}List = repository.findBy${attribute.attribute_name?cap_first}(Long.parseLong(searchLabel));
    <#elseif attribute.attribute_java == "Double">
                ${concept_varname}List = repository.findBy${attribute.attribute_name?cap_first}(Double.parseDouble(searchLabel));
    </#if>
                break;
</#list>
            default:
                throw new Exception("field ${concept_name}." + searchField + " is not searchable");
        }

        log.info("SELECT ${concept_name} WHERE {} = {}; Found ", searchField, searchValue, ${concept_varname}List.size());
        return ResponseEntity.ok().body(${concept_varname}List);
    }

	@RequestMapping(value = "/${concept_apipath}/all", method = RequestMethod.GET)
    public ResponseEntity<List<${concept_name}>> getAll${concept_name}(
        ) throws Exception {
        List<${concept_name}> ${concept_varname}List = repository.findAll();
        log.info("SELECT ${concept_name} all");
        return ResponseEntity.ok().body(${concept_varname}List);
    }

    @RequestMapping(value = "/${concept_apipath}/page/{page}", method = RequestMethod.GET)
    public ResponseEntity<List<${concept_name}>> getAll${concept_name}ByPage(
        @PathVariable(value = "page") Integer pageNum,
        @RequestParam(value = "perPage", defaultValue = "10") Integer perPage
        ) throws Exception {

        if(pageNum == null || pageNum <= 0)
            pageNum = 1;
        Integer offset = (pageNum - 1) * perPage;

        List<${concept_name}> ${concept_varname}List = repository.findAllByLimitAndOffset(perPage, offset);
        log.info("SELECT ${concept_name} WHERE pageNum={}", pageNum);
        return ResponseEntity.ok().body(${concept_varname}List);
    }